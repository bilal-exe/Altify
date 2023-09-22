package bilal.altify.data.spotify

import bilal.altify.domain.sources.VolumeSource
import com.spotify.android.appremote.api.ConnectApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class VolumeSourceImpl(
    private val connectApi: ConnectApi
) : VolumeSource {

    override val volume: Flow<Float> = callbackFlow {

        trySend(0f)

        val subscription = connectApi.subscribeToVolumeState()
            .setEventCallback {
                trySend(it.mVolume)
            }
            .setErrorCallback {
                throw VolumeSource.VolumeSourceException(it.localizedMessage)
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