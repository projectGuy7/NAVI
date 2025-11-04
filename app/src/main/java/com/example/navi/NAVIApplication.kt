package com.example.navi

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import ru.dgis.sdk.DGis

@HiltAndroidApp
class NAVIApplication: Application() {
    lateinit var sdkContext: ru.dgis.sdk.Context

    override fun onCreate() {
        super.onCreate()

        sdkContext = DGis.initialize(
            this
        )

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}