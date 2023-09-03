package bilal.altify

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AltifyApp : Application() {

    override fun onCreate() {
        val mChannel = NotificationChannel(
            VOLUME_CHANNEL_ID, "Volume", NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
        super.onCreate()
    }

    companion object {
        const val VOLUME_CHANNEL_ID = "VOLUME_CHANNEL_ID"
    }
}