package bilal.altify.data

import com.spotify.android.appremote.api.ConnectApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

class Volume(
    private val connectApi: ConnectApi
) {

    val volume = callbackFlow {
        val subscription = connectApi.subscribeToVolumeState().setEventCallback { trySend(it) }
        awaitClose { subscription.cancel() }
    }
        .stateIn(CoroutineScope(IO), SharingStarted.WhileSubscribed(5000), null)

    suspend fun increaseVolume() {
        connectApi.connectIncreaseVolume()
    }

    suspend fun decreaseVolume() {
        connectApi.connectDecreaseVolume()
        
    }

}