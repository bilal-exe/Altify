package bilal.altify.data.spotify

import bilal.altify.domain.repository.VolumeRepository
import com.spotify.android.appremote.api.ConnectApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

class VolumeRepositoryImpl(
    private val connectApi: ConnectApi
) : VolumeRepository {

    override fun getVolume(): Flow<Float> = callbackFlow {

        trySend(0f)

        val subscription = connectApi.subscribeToVolumeState()
            .setEventCallback {
                trySend(it.mVolume)
            }
            .setErrorCallback {
                throw Exception("Error callback")
            }

        awaitClose { subscription.cancel() }

    }.stateIn(CoroutineScope(IO), SharingStarted.WhileSubscribed(), 0f)

    override fun increaseVolume() {
        connectApi.connectIncreaseVolume()
    }

    override fun decreaseVolume() {
        connectApi.connectDecreaseVolume()
    }

    override fun setVolume(volume: Float) {
        connectApi.connectSetVolume(volume)
    }

}