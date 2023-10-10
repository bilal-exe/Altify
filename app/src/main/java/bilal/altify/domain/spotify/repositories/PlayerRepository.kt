package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.spotify.model.AltPlayerStateAndContext
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    val playerStateAndContext: Flow<AltPlayerStateAndContext>

    fun pauseResume(isPaused: Boolean)

    fun skipPrevious()

    fun skipNext()

    fun play(uri: String)

    fun seek(position: Long)

    fun seekRelative(position: Long)

    fun addToQueue(uri: String)

    fun skipToTrack(uri: String, index: Int)

    class PlayerSourceException(override val message: String?): Exception(message)

}