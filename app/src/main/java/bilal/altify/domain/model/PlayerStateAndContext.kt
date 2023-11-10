package bilal.altify.domain.model

data class PlayerStateAndContext(
    val track: Item.Track? = null,
    val isPaused: Boolean = true,
    val position: Long = 0L,
    val context: PlayerContext? = null,
    val repeatMode: RepeatMode,
    val isShuffling: Boolean
)

enum class RepeatMode {
    OFF, CONTEXT, TRACK
}