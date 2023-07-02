package bilal.altify.domain.repository

import bilal.altify.domain.model.AltPlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun getPlayerState(): Flow<AltPlayerState>

    fun pauseResume(isPaused: Boolean)

    fun skipPrevious()

    fun skipNext()

    fun play(uri: String)

    fun seek(position: Long)

    fun addToQueue(uri: String)

    fun skipToTrack(uri: String, index: Int)

}