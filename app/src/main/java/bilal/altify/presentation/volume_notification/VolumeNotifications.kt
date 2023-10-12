package bilal.altify.presentation.volume_notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import bilal.altify.R
import bilal.altify.presentation.util.collectLatestOn
import bilal.altify.presentation.util.quickCheckPerms
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class VolumeNotifications @Inject constructor(private val context: Context) {
    
    private var builder = NotificationCompat.Builder(context, VOLUME_CHANNEL_ID)
        .setSmallIcon(R.drawable.volume_up)
        .setContentTitle("Spotify Volume")
        .setContentText("Set the volume of Spotify")
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setOnlyAlertOnce(true)
        .setVibrate(null)


    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        val mChannel = NotificationChannel(
            VOLUME_CHANNEL_ID, "Volume", NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = context.getSystemService(Application.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    @SuppressLint("MissingPermission")
    fun show(scope: CoroutineScope, volume: Flow<Float>) {
        with(NotificationManagerCompat.from(context)) {
            volume
                .distinctUntilChanged()
                .collectLatestOn(scope) { vol ->
                    builder.setProgress(MAX_PROGRESS, (vol * 100).toInt(), false)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        if (context.quickCheckPerms(Manifest.permission.POST_NOTIFICATIONS))
                            notify(notificationId, builder.build())
                }
        }
    }

    fun delete() {
        notificationManager.cancelAll()
    }

    companion object {
        const val VOLUME_CHANNEL_ID = "VOLUME_CHANNEL_ID"
        private var notificationId = 0
        private const val MAX_PROGRESS = 100
    }

}