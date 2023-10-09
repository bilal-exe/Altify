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
    ): AltifyUIState
}