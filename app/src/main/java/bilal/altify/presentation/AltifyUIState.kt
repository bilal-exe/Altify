package bilal.altify.presentation

import bilal.altify.domain.spotify.repositories.AltifyRepositories
import bilal.altify.presentation.prefrences.AltPreferencesState

sealed interface AltifyUIState {
    data class Disconnected(val message: String? = null): AltifyUIState
    object Connecting: AltifyUIState
    data class Success(
        val preferences: AltPreferencesState = AltPreferencesState(),
        val repositories: AltifyRepositories,
    ): AltifyUIState
}