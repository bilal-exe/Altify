package bilal.altify.presentation

import android.graphics.Bitmap
import bilal.altify.data.dataclasses.AltListItem
import bilal.altify.data.dataclasses.AltPlayerContext
import bilal.altify.data.dataclasses.AltTrack

sealed interface AltifyUIState {

    data class Disconnected(val message: String? = null): AltifyUIState

    object Connecting: AltifyUIState

    data class Connected(
        val track: AltTrack? = null,
        val isPaused: Boolean = true,
        val playbackPosition: Long = 0L,
        val playerContext: AltPlayerContext? = null,
        val volume: Float = 0f,
        val listItems: Array<AltListItem>? = null,
        val artwork: Bitmap? = null
    ): AltifyUIState

}