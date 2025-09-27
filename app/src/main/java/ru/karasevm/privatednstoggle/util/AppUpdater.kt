package ru.karasevm.privatednstoggle.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.karasevm.privatednstoggle.BuildConfig
import ru.karasevm.privatednstoggle.R
import java.io.File
import java.io.FileOutputStream

object AppUpdater {

    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getUpdate(context: Context): Update? {
        return withContext(Dispatchers.IO) {
            try {
                val url = "https://api.github.com/repos/silvzr/PrivateDNSAndroidTV/releases/latest"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext null
                }
                val body = response.body?.string() ?: return@withContext null
                val release = json.decodeFromString<GithubReleaseResponse>(body)

                val currentVersion = BuildConfig.VERSION_NAME.removePrefix("v")
                if (release.tagName.removePrefix("v") != currentVersion) {
                    val asset = release.assets.firstOrNull { it.name.endsWith(".apk") }
                    if (asset != null) {
                        return@withContext Update(release.tagName, asset.browserDownloadUrl)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext null
        }
    }

    suspend fun downloadUpdate(context: Context, url: String): File? {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext null
                }
                val body = response.body
                if (body != null) {
                    val file = File(context.cacheDir, "update.apk")
                    FileOutputStream(file).use { output ->
                        body.byteStream().use { input ->
                            input.copyTo(output)
                        }
                    }
                    return@withContext file
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext null
        }
    }

    fun installApk(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun cleanup(context: Context) {
        val file = File(context.cacheDir, "update.apk")
        if (file.exists()) {
            file.delete()
        }
    }

    data class Update(val version: String, val downloadUrl: String)

    @Serializable
    data class GithubReleaseResponse(
        @SerialName("tag_name")
        val tagName: String,
        val assets: List<Asset>
    ) {
        @Serializable
        data class Asset(
            val name: String,
            @SerialName("browser_download_url")
            val browserDownloadUrl: String
        )
    }
}
