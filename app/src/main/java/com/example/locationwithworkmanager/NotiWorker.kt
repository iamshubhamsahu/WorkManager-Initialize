package com.example.locationwithworkmanager

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotiWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val context = applicationContext
        return try {
            Log.d(TAG, "doWork: Called!!")
            Utils.sendNotification(context)
            Result.success()
        } catch (throwable: Throwable) {
            Log.d(TAG, "Error Sending Notification" + throwable.message)
            Result.failure()
        }
    }

    companion object {
        private val TAG = NotiWorker::class.java.name
    }
}