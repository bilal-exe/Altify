package bilal.altify.data.spotify.respoitories

import android.util.Log
import bilal.altify.data.mappers.toModel
import bilal.altify.data.mappers.toSpotifyUri
import bilal.altify.data.spotify.model.NetworkDevice
import bilal.altify.data.spotify.sources.PlayerNetworkSource
import bilal.altify.domain.model.Device
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.spotify.repositories.PlayerRepository
import com.spotify.android.appremote.api.PlayerApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class PlayerRepositoryImpl(
    private val playerApi: PlayerApi,
    private val playerNetworkSource: PlayerNetworkSource,
) : PlayerRepository {

    override val playerState = callbackFlow {
        val subscription = playerApi.subscribeToPlayerState()
            .setEventCallback {
                trySend(it.toModel())
            }
            .setErrorCallback {
                Log.d("Error", it.message.toString())
                throw PlayerRepository.PlayerSourceException(it.localizedMessage)
            }
        awaitClose {
            subscription.cancel()
        }
    }


    override val playerContext = callbackFlow {

        val subscription = playerApi.subscribeToPlayerContext()
            .setEventCallback {
                trySend(it.toModel())
            }
            .setErrorCallback {
                Log.d("Error", it.message.toString())
                throw PlayerRepository.PlayerSourceException(it.localizedMessage)
            }

        awaitClose { subscription.cancel() }

    }

    override fun pauseResume(isPaused: Boolean) {
        when (isPaused) {
            true -> playerApi.resume()
            false -> playerApi.pause()
        }
    }

    override fun play(remoteId: RemoteId) {
        playerApi.play(remoteId.toSpotifyUri())
    }

    override fun addToQueue(remoteId: RemoteId) {
        playerApi.queue(remoteId.toSpotifyUri())
    }

    override fun seek(position: Long) {
        playerApi.seekTo(position)
    }

    override fun seekRelative(position: Long) {
        playerApi.seekToRelativePosition(position)
    }

    override fun skipNext() {
        playerApi.skipNext()
    }

    override fun skipPrevious() {
        playerApi.skipPrevious()
    }

    override fun skipToTrack(remoteId: RemoteId, index: Int) {
        playerApi.skipToIndex(remoteId.toSpotifyUri(), index)
    }

    override fun toggleRepeat() {
        playerApi.toggleRepeat()
    }

    override fun toggleShuffle() {
        playerApi.toggleShuffle()
    }

    override suspend fun getAvailableDevices(): List<Device> =
        playerNetworkSource.getAvailableDevices("").map(NetworkDevice::toDevice)


    override suspend fun transferPlaybackToDevice(device: Device) {
        TODO("Not yet implemented")
    }
}