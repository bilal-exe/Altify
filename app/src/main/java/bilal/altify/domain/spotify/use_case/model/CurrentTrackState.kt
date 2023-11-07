package bilal.altify.domain.spotify.use_case.model

import android.graphics.Bitmap
import bilal.altify.domain.model.LibraryState
import bilal.altify.domain.model.PlayerContext
import bilal.altify.domain.model.RepeatMode

data class CurrentTrackState(
    val track: Track? = null,
    val isPaused: Boolean = true,
    val playbackPosition: Long = 0L,
    val playerContext: PlayerContext? = null,
    val volume: Float = 0f,
    val artwork: Bitmap? = null,
    val libraryState: LibraryState? = null,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val isShuffled: Boolean = false
)
