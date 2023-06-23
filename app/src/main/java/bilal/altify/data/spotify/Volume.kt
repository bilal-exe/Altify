package bilal.altify.data.spotify

import android.util.Log
import com.spotify.android.appremote.api.ConnectApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class Volume(
    private val connectApi: ConnectApi
) {

    val volume = callbackFlow {
        val subscription = connectApi.subscribeToVolumeState()
            .setEventCallback { trySend(it) }
            .setErrorCallback {
                throw Exception("Error callback")
            }
        Log.d("Spotify", "volume received")
        awaitClose { subscription.cancel() }
    }
        .flowOn(IO)
        .stateIn(CoroutineScope(IO), SharingStarted.WhileSubscribed(5000), null)

    suspend fun increaseVolume() {
        connectApi.connectIncreaseVolume()
    }

    suspend fun decreaseVolume() {
        connectApi.connectDecreaseVolume()
    }

    suspend fun setVolume(volume: Float) {
        connectApi.connectSetVolume(volume)
    }

}