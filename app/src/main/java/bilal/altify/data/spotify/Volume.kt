package bilal.altify.data.spotify

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
            .setEventCallback { trySend(it.mVolume) }
            .setErrorCallback {
                throw Exception("Error callback")
            }
        awaitClose { subscription.cancel() }
    }
        .flowOn(IO)
        .stateIn(CoroutineScope(IO), SharingStarted.WhileSubscribed(), 0f)

    fun increaseVolume() {
        connectApi.connectIncreaseVolume()
    }

    fun decreaseVolume() {
        connectApi.connectDecreaseVolume()
    }

    fun setVolume(volume: Float) {
        connectApi.connectSetVolume(volume)
    }

}