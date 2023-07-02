package bilal.altify.presentation

import android.graphics.Bitmap
import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.model.AltPlayerContext
import bilal.altify.domain.model.AltTrack
import bilal.altify.presentation.prefrences.AltPreferencesState

sealed interface AltifyConnectionState {

    data class Disconnected(val message: String? = null): AltifyConnectionState

    object Connecting: AltifyConnectionState

    object Success: AltifyConnectionState

}

data class AltifyUIState(
    val connectionState: AltifyConnectionState = AltifyConnectionState.Connecting,
    val preferences: AltPreferencesState = AltPreferencesState(),
    val track: AltTrack? = null,
    val isPaused: Boolean = true,
    val playbackPosition: Long = 0L,
    val playerContext: AltPlayerContext? = null,
    val volume: Float = 0f,
    val listItems: List<AltListItem>? = null,
    val artwork: Bitmap? = null
)