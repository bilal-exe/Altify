package bilal.altify.domain.sources

import bilal.altify.domain.model.AltPlayerStateAndContext
import kotlinx.coroutines.flow.Flow

interface PlayerSource {

    val playerStateAndContext: Flow<AltPlayerStateAndContext>

    fun pauseResume(isPaused: Boolean)

    fun skipPrevious()

    fun skipNext()

    fun play(uri: String)

    fun seek(position: Long)

    fun seekRelative(position: Long)

    fun addToQueue(uri: String)

    fun skipToTrack(uri: String, index: Int)

}