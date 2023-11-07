package bilal.altify.domain.spotify.model

data class PlayerStateAndContext(
    val track: Track? = null,
    val isPaused: Boolean = true,
    val position: Long = 0L,
    val context: PlayerContext? = null,
    val repeatMode: RepeatMode,
    val isShuffling: Boolean
)

enum class RepeatMode {
    OFF, CONTEXT, TRACK
}