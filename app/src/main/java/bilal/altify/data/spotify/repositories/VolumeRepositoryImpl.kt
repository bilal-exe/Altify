package bilal.altify.data.spotify.repositories

import bilal.altify.domain.spotify.repositories.appremote.VolumeRepository
import com.spotify.android.appremote.api.ConnectApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class VolumeRepositoryImpl(
    private val connectApi: ConnectApi
) : VolumeRepository {

    override val volume: Flow<Float> = callbackFlow {

        trySend(0f)

        val subscription = connectApi.subscribeToVolumeState()
            .setEventCallback {
                trySend(it.mVolume)
            }
            .setErrorCallback {
                throw VolumeRepository.VolumeSourceException(it.localizedMessage)
            }

        awaitClose { subscription.cancel() }

    }

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