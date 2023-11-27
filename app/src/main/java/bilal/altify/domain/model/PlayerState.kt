package bilal.altify.domain.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class PlayerState(
    val track: Item.Track? = null,
    val isPaused: Boolean = true,
    val position: Duration = 0.milliseconds,
    val repeatMode: RepeatMode,
    val isShuffling: Boolean
)

enum class RepeatMode {
    OFF, CONTEXT, TRACK
}