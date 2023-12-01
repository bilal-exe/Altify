package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.model.Device
import bilal.altify.domain.model.PlayerContext
import bilal.altify.domain.model.PlayerState
import bilal.altify.domain.model.RemoteId
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    val playerState: Flow<PlayerState>

    val playerContext: Flow<PlayerContext>

    fun pauseResume(isPaused: Boolean)

    fun skipPrevious()

    fun skipNext()

    fun play(remoteId: RemoteId)

    fun seek(position: Long)

    fun seekRelative(position: Long)

    fun addToQueue(remoteId: RemoteId)

    fun skipToTrack(remoteId: RemoteId, index: Int)

    fun toggleRepeat()

    fun toggleShuffle()

    suspend fun getAvailableDevices(): List<Device>

    suspend fun transferPlaybackToDevice(device: Device)

    class PlayerSourceException(override val message: String?): Exception(message)

}