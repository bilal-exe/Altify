package bilal.altify.domain.spotify.repositories.appremote

import bilal.altify.domain.model.PlayerStateAndContext
import bilal.altify.domain.model.RemoteId
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    val playerStateAndContext: Flow<PlayerStateAndContext>

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

    class PlayerSourceException(override val message: String?): Exception(message)

}