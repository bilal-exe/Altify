package bilal.altify.presentation

import android.graphics.Bitmap
import bilal.altify.domain.controller.AltifySources
import bilal.altify.domain.model.AltLibraryState
import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.model.AltPlayerContext
import bilal.altify.domain.model.AltTrack
import bilal.altify.presentation.prefrences.AltPreferencesState

sealed interface AltifyConnectionState {
    data class Disconnected(val message: String? = null): AltifyConnectionState
    object Connecting: AltifyConnectionState
    data class Success(val repositories: AltifySources): AltifyConnectionState
}

data class AltifyUIState(
    val connectionState: AltifyConnectionState = AltifyConnectionState.Connecting,
    val preferences: AltPreferencesState = AltPreferencesState(),
    val currentTrackState: CurrentTrackState = CurrentTrackState(),
    val browserState: BrowserState = BrowserState()
)

data class CurrentTrackState(
    val track: AltTrack? = null,
    val isPaused: Boolean = true,
    val playbackPosition: Long = 0L,
    val playerContext: AltPlayerContext? = null,
    val volume: Float = 0f,
    val artwork: Bitmap? = null,
    val libraryState: AltLibraryState? = null,
)

data class BrowserState(
    val listItems: List<AltListItem> = emptyList(),
    val libraryState: Map<String, AltLibraryState> = emptyMap(),
    val thumbnailMap: Map<String, Bitmap> = emptyMap(),
)