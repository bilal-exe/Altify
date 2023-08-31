package bilal.altify.domain.model

data class AltPlayerStateAndContext(
    val track: AltTrack? = null,
    val isPaused: Boolean = true,
    val position: Long = 0L,
    val context: AltPlayerContext? = null
)