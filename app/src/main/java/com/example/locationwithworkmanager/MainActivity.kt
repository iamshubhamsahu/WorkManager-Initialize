package com.example.locationwithworkmanager

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    var workManager: WorkManager? = null
    var btnStartOneTimeRequest: AppCompatButton? = null
    var btnStartPeriodicRequest: AppCompatButton? = null


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workManager = WorkManager.getInstance(this)
        btnStartOneTimeRequest = findViewById(R.id.btn_one_time)
        btnStartPeriodicRequest = findViewById(R.id.btn_periodic_time)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            checkPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            ) {}

        btnStartOneTimeRequest?.setOnClickListener {
            val constraints: Constraints = Constraints.Builder().setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.CONNECTED).build()

            val oneTimeWorkRequest: OneTimeWorkRequest =
                OneTimeWorkRequest.Builder(NotiWorker::class.java).setConstraints(constraints)
                    .build()
            workManager?.enqueue(oneTimeWorkRequest)
        }

        btnStartPeriodicRequest?.setOnClickListener {
            val constraints: Constraints = Constraints.Builder().setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.CONNECTED).build()

            val periodicWorkRequest: PeriodicWorkRequest =
                PeriodicWorkRequest.Builder(NotiWorker::class.java, 15, TimeUnit.MINUTES)
                    .setConstraints(constraints).build()
            workManager!!.enqueue(periodicWorkRequest)
        }
    }
}