package bilal.altify.domain.spotify.model

import android.graphics.Bitmap

data class CurrentTrackState(
    val track: AltTrack? = null,
    val isPaused: Boolean = true,
    val playbackPosition: Long = 0L,
    val playerContext: AltPlayerContext? = null,
    val volume: Float = 0f,
    val artwork: Bitmap? = null,
    val libraryState: AltLibraryState? = null,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val isShuffled: Boolean = false
)
