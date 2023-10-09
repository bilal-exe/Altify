package bilal.altify.domain.spotify.model

data class AltPlayerStateAndContext(
    val track: AltTrack? = null,
    val isPaused: Boolean = true,
    val position: Long = 0L,
    val context: AltPlayerContext? = null
)