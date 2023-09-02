package com.example.imageshering.works

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.work.*
import com.example.imageshering.R
import com.google.android.material.snackbar.Snackbar

fun downloadImage(
    context: Context,
    viewLifecycleOwner: LifecycleOwner,
    view: View,
    url: String,
    id: String,
) {
    val workerConstraint = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
    val photoData = Data.Builder()
        .putString("url", url)
        .putString("id", id)
        .build()
    val downloadWorkRequest = OneTimeWorkRequest
        .Builder(DownloadWorker::class.java)
        .setConstraints(workerConstraint)
        .setInputData(photoData)
        .addTag(id)
        .build()
    val workManager = WorkManager.getInstance(context)
    workManager.beginUniqueWork(
        id,
        ExistingWorkPolicy.KEEP,
        downloadWorkRequest
    ).enqueue()
    workManager.getWorkInfoByIdLiveData(downloadWorkRequest.id)
        .observe(viewLifecycleOwner) { workInfo ->
            workInfo?.state?.let {
                if (it == WorkInfo.State.SUCCEEDED) {
                    val uri = Uri.parse(workInfo.outputData.getString("uri"))
                    Snackbar.make(
                        view,
                        context.getString(R.string.image_downloaded),
                        Snackbar.LENGTH_SHORT
                    )
                        .setAction(context.getString(R.string.open)) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(uri, "image/*")
                            context.startActivity(intent)
                        }
                        .show()
                }
            }
        }
}