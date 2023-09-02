package com.example.imageshering.works

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.imageshering.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class DownloadWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    private val notificationManager =
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
    private lateinit var photoId: String
    private lateinit var photoUrl: String
    private lateinit var builder: NotificationCompat.Builder

    @SuppressLint("Range", "UnspecifiedImmutableFlag")
    override suspend fun doWork(): Result {
        photoUrl = workerParameters.inputData.getString("url") ?: return Result.failure()
        photoId = workerParameters.inputData.getString("id") ?: return Result.failure()
        builder = NotificationCompat.Builder(context, photoId)
            .setSmallIcon(R.drawable.baseline_download_24_day)
            .setContentTitle(context.getString(R.string.channel_description))
            .setProgress(10, 0, false)
        createNotificationChannel(photoId)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(photoUrl))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(photoUrl)
            .setDescription(photoUrl)
        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query()
        query.setFilterById(downloadId)
        notificationManager.notify(photoId, 0, builder.build())
        var lastProgress = 0
        while (true) {
            val cursor = downloadManager.query(query)
            cursor.moveToFirst()
            val bytesDownloaded =
                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            val totalBytes =
                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
            val progress = (bytesDownloaded * 100f / totalBytes).toInt()
            builder.setProgress(100, progress, false)
                .setContentText("$progress %")
            notificationManager.notify(photoId, 0, builder.build())
            when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    val uri =
                        Uri.parse(cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)))
                    val contentResolver = context.contentResolver
                    val contentValues = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, photoId)
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    }
                    val imageUri = contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )
                    val inputStream = contentResolver.openInputStream(uri)
                    val outputStream = imageUri?.let {
                        contentResolver.openOutputStream(it)
                    }
                    inputStream?.copyTo(outputStream!!)
                    withContext(Dispatchers.IO) {
                        inputStream?.close()
                        outputStream?.close()
                    }
                    MediaScannerConnection.scanFile(context, arrayOf(photoId), null, null)

                    cursor.close()
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(imageUri, "image/*")
                    val pendingIntent: PendingIntent =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            PendingIntent.getActivity(
                                context,
                                0,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            PendingIntent.getActivity(
                                context,
                                0,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                            )
                        }
                    notificationManager.notify(
                        photoId,
                        0,
                        builder
                            .setProgress(0, 0, false)
                            .setContentText(context.getString(R.string.image_downloaded))
                            .addAction(
                                R.drawable.baseline_open_in_new_24,
                                context.getString(R.string.open),
                                pendingIntent
                            )
                            .build()
                    )
                    val outputDate = Data.Builder()
                        .putString("uri", imageUri.toString())
                        .build()
                    return Result.success(outputDate)
                }
                DownloadManager.STATUS_FAILED -> {
                    val reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                    notificationManager.notify(
                        photoId,
                        0,
                        builder
                            .setProgress(0, 0, false)
                            .setContentText(context.getString(R.string.failed_download_msg, reason))
                            .build()
                    )
                    cursor.close()
                    return Result.failure()
                }
            }
            cursor.close()
            if (progress != lastProgress) {
                lastProgress = progress
            }
            delay(100)
        }
    }

    private fun createNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}