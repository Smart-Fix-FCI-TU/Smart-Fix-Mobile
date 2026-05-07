import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}
val baseUrl = localProperties.getProperty("BASE_URL")
    ?: throw GradleException("BASE_URL is missing in local.properties")

android {
    namespace = "com.fcitu.smartfix"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.fcitu.smartfix"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // ── Core ──────────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.datetime)

    // ── Compose ───────────────────────────────────────
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.compose.navigation)
    implementation(libs.compose.lifecycle.runtime)

    // ── Squircle Shape ───────────────────────────────────────
    implementation(libs.squircle.shape)

    // ── Koin ──────────────────────────────────────────
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // ── Networking ────────────────────────────────────
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.core)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp.logging)

    // ── Serialization ─────────────────────────────────
    implementation(libs.kotlinx.serialization.json)

    // ── DataStore ─────────────────────────────────────
    implementation(libs.androidx.datastore.preferences)

    // ── Coroutines ────────────────────────────────────
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // ── Image Loading ─────────────────────────────────
    implementation(libs.coil.compose)

    // ── Testing ───────────────────────────────────────
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}