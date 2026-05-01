# Room Reference — Entities, DAOs, Database, Migrations

## Table of Contents
1. Entity patterns
2. DAO patterns (suspend vs Flow)
3. Type converters
4. Database class + AppDatabase singleton
5. Migrations
6. FTS (Full-Text Search)
7. Relations (@Embedded, @Relation, @Transaction)
8. Common pitfalls

---

## 1. Entity Patterns

```kotlin
// data/local/entity/UserEntity.kt
@Entity(
    tableName = "users",
    indices = [
        Index(value = ["phone"], unique = true),
        Index(value = ["created_at"]),               // index columns you ORDER/WHERE by
    ]
)
data class UserEntity(
    @PrimaryKey val id: String,                      // String UUID preferred; use AUTOINCREMENT only for pure-local IDs
    @ColumnInfo(name = "full_name") val name: String,
    val phone: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "is_synced") val isSynced: Boolean = false,
)
```

**Rules:**
- Always use `@ColumnInfo(name = "snake_case")` for multi-word fields
- Add `@Index` for any column used in WHERE, ORDER BY, or JOIN
- Prefer `String` UUID primary keys for server-synced data; `Long` with AUTOINCREMENT for local-only
- Include `isSynced: Boolean` / `updatedAt: Long` on entities that are cached from server (offline-first)

---

## 2. DAO Patterns

### Choosing suspend vs Flow
| Use case | Return type |
|---|---|
| Insert / update / delete | `suspend fun` |
| One-shot query | `suspend fun` |
| Reactive query (UI observes) | `fun` returning `Flow<T>` |
| Paginated query | `fun` returning `PagingSource<Int, T>` |

```kotlin
// data/local/dao/UserDao.kt
@Dao
interface UserDao {

    // --- Writes (always suspend) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNotExists(user: UserEntity): Long   // returns -1 if ignored

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    // --- Reactive reads (Flow, no suspend) ---
    @Query("SELECT * FROM users ORDER BY created_at DESC")
    fun observeAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    fun observeById(id: String): Flow<UserEntity?>

    // --- One-shot reads (suspend) ---
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: String): UserEntity?

    @Query("SELECT * FROM users WHERE is_synced = 0")
    suspend fun getUnsynced(): List<UserEntity>

    // --- Paging ---
    @Query("SELECT * FROM users ORDER BY full_name ASC")
    fun pagingSource(): PagingSource<Int, UserEntity>
}
```

---

## 3. Type Converters

```kotlin
// data/local/converter/Converters.kt
class Converters {
    @TypeConverter
    fun fromList(list: List<String>): String = Json.encodeToString(list)

    @TypeConverter
    fun toList(json: String): List<String> = Json.decodeFromString(json)

    @TypeConverter
    fun fromInstant(instant: Instant?): Long? = instant?.toEpochMilli()

    @TypeConverter
    fun toInstant(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(it) }
}
```

Register on the `@Database` class: `@TypeConverters(Converters::class)`

---

## 4. Database Class

```kotlin
// data/local/AppDatabase.kt
@Database(
    entities = [UserEntity::class, OrderEntity::class],
    version = 2,
    exportSchema = true,                            // ALWAYS true — enables migration testing
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smartfix.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()   // remove in production after migration is stable
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
```

---

## 5. Migrations

```kotlin
// data/local/migration/Migrations.kt
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE users ADD COLUMN is_synced INTEGER NOT NULL DEFAULT 0")
    }
}
```

**Rules:**
- One `Migration` object per version bump
- Always `NOT NULL DEFAULT` for new non-null columns
- Test with `MigrationTestHelper` in androidTest

---

## 6. FTS (Full-Text Search)

```kotlin
@Fts4(contentEntity = UserEntity::class)
@Entity(tableName = "users_fts")
data class UserFtsEntity(
    val fullName: String,
    val phone: String,
)

// DAO query
@Query("SELECT * FROM users JOIN users_fts ON users.rowid = users_fts.rowid WHERE users_fts MATCH :query")
fun search(query: String): Flow<List<UserEntity>>
```

---

## 7. Relations

```kotlin
// One-to-many with @Transaction
data class UserWithOrders(
    @Embedded val user: UserEntity,
    @Relation(parentColumn = "id", entityColumn = "user_id")
    val orders: List<OrderEntity>,
)

@Dao
interface UserDao {
    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    fun observeUserWithOrders(userId: String): Flow<UserWithOrders?>
}
```

Always annotate multi-table queries with `@Transaction` to prevent partial reads across writes.

---

## 8. Common Pitfalls

| Pitfall | Fix |
|---|---|
| `suspend` on Flow-returning DAO fun | Remove `suspend` — Room emits lazily |
| Missing `@Transaction` on `@Relation` | Always add it |
| No index on foreign key column | Add `@Index` |
| `runBlocking` to call DAO | Use `withContext(Dispatchers.IO)` or let Room handle dispatcher |
| `OnConflictStrategy.ABORT` (default) on upsert | Use `REPLACE` or `IGNORE` intentionally |
| Mutable list in entity | Use `List<T>` with TypeConverter, not `MutableList` |