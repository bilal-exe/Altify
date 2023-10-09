package bilal.altify.presentation

import android.graphics.Bitmap
import bilal.altify.domain.spotify.repositories.AltifyRepositories
import bilal.altify.domain.spotify.model.AltLibraryState
import bilal.altify.domain.spotify.model.AltListItems
import bilal.altify.domain.spotify.model.AltPlayerContext
import bilal.altify.domain.spotify.model.AltTrack
import bilal.altify.presentation.prefrences.AltPreferencesState
import java.util.Stack

sealed interface AltifyUIState {
    data class Disconnected(val message: String? = null): AltifyUIState
    object Connecting: AltifyUIState
    data class Success(
        val preferences: AltPreferencesState = AltPreferencesState(),
        val repositories: AltifyRepositories,
//        val trackState: CurrentTrackState = CurrentTrackState(),
//        val browserState: BrowserState = BrowserState()
    ): AltifyUIState
}

//data class CurrentTrackState(
//    val track: AltTrack? = null,
//    val isPaused: Boolean = true,
//    val playbackPosition: Long = 0L,
//    val playerContext: AltPlayerContext? = null,
//    val volume: Float = 0f,
//    val artwork: Bitmap? = null,
//    val libraryState: AltLibraryState? = null,
//)

//data class BrowserState(
//    val listItems: AltListItems = AltListItems(),
//    val libraryState: Map<String, AltLibraryState> = emptyMap(),
//    val thumbnailMap: Map<String, Bitmap> = emptyMap(),
//    val uriHistory: Stack<String> = Stack()
//)