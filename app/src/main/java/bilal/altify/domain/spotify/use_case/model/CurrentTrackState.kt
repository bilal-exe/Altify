package bilal.altify.domain.spotify.use_case.model

import android.graphics.Bitmap
import bilal.altify.domain.spotify.model.LibraryState
import bilal.altify.domain.spotify.model.PlayerContext
import bilal.altify.domain.spotify.model.RepeatMode
import bilal.altify.domain.spotify.model.Track

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
