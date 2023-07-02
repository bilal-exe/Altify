package bilal.altify.domain.model

data class AltPlayerState(
    val track: AltTrack? = null,
    val isPaused: Boolean = true,
    val position: Long = 0L,
    val context: AltPlayerContext? = null
){
    companion object {
        const val INTERPOLATION_FREQUENCY_MS = 500L
    }
}