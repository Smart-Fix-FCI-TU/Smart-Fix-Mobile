package com.fcitu.smartfix.ui.utils

import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

 suspend fun openImageInExternalApp(context: android.content.Context, imageUrl: String) {
    try {
        val imageFile = withContext(Dispatchers.IO) {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            // We tell the server we're a regular browser so they don't reject us.
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
            connection.instanceFollowRedirects = true

            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.doInput = true
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw Exception("Server returned HTTP ${connection.responseCode}")
            }

            val fileName = "shared_image_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, fileName)

            connection.inputStream.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file
        }

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)

        withContext(Dispatchers.Main) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(intent, "Open image with"))
        }

    } catch (e: Exception) {
        e.printStackTrace()
//  If the download fails, open it in the browser        withContext(Dispatchers.Main) {
        Toast.makeText(context, "Cannot open in Gallery, opening in browser...", Toast.LENGTH_SHORT)
            .show()

        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, imageUrl.toUri())
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(browserIntent)
        } catch (ex: Exception) {
            Toast.makeText(context, "Failed to open image", Toast.LENGTH_SHORT).show()
        }
    }
}
