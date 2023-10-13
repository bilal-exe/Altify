package bilal.altify.presentation

import bilal.altify.domain.spotify.repositories.AltifyRepositories
import bilal.altify.presentation.prefrences.AltPreferencesState

sealed interface AltifyUIState {
    data class Disconnected(val error: Error) : AltifyUIState
    object Connecting : AltifyUIState
    data class Success(
        val preferences: AltPreferencesState = AltPreferencesState(),
        val repositories: AltifyRepositories,
        val token: String
    ) : AltifyUIState
}

sealed interface Error {
    data class SpotifyConnector(val message: String?) : Error
    object APIToken : Error
}