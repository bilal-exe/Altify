package bilal.altify.presentation.volume_notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.Notification.MediaStyle
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.Player
import bilal.altify.AltifyApp.Companion.VOLUME_CHANNEL_ID
import bilal.altify.R
import bilal.altify.presentation.util.quickCheckPerms
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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


    @SuppressLint("MissingPermission")
    fun show(scope: CoroutineScope, volume: Flow<Float>) {
        with(NotificationManagerCompat.from(context)) {
            scope.launch {
                volume.collectLatest { vol ->
                    builder.setProgress(MAX_PROGRESS, (vol * 100).toInt(), false)
                    Log.d("Vol", "${(vol * 100).toInt()} $MAX_PROGRESS")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        if (context.quickCheckPerms(Manifest.permission.POST_NOTIFICATIONS))
                            notify(notificationId, builder.build())
                }
            }
        }
    }

    fun delete() {
        notificationManager.cancelAll()
    }

    companion object {
        private var notificationId = 0
        private const val MAX_PROGRESS = 100
    }

}