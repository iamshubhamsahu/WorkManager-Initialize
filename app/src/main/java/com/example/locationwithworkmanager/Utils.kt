package com.example.locationwithworkmanager

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

object Utils {
    const val NOTIFICATION_ID = 22
    private const val CHANNEL_ID = "notify"
    private const val CHANNEL_NAME = "workmanager-reminder"

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotificationPermission")
    fun sendNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel1 =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        channel1.enableVibration(true)
        channel1.enableLights(true)
        channel1.lightColor = com.google.android.material.R.color.design_default_color_primary
        channel1.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager.createNotificationChannel(channel1)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("WorkManager Sample")
            .setContentText("WorkManager Started")
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
        notificationManager.notify(1, builder.build())
    }
}

fun Context.checkPermissions(vararg permission: String, returnData: (Boolean) -> Unit) = try {
    Dexter.withContext(this)
        .withPermissions(*permission)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if (report.areAllPermissionsGranted()) {
                        returnData(true)
                    } else if (report.isAnyPermissionPermanentlyDenied) {
                        AlertDialog.Builder(this@checkPermissions).apply {
                            setTitle("Bidjones")
                            setMessage("App need's permission. Please grant permission's.")
                            setPositiveButton("Grant") { _, _ ->
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", packageName, null)
                                    startActivity(this)
                                }
                            }
                            show()
                        }
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }
        })
        .check()
} catch (e: Exception) {
    e.printStackTrace()
}