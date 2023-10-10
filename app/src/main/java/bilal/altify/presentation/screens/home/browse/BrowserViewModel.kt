package bilal.altify.presentation.screens.home.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.domain.prefrences.PreferencesRepository
import bilal.altify.domain.spotify.model.BrowserState
import bilal.altify.domain.spotify.remote.SpotifyConnectorResponse
import bilal.altify.domain.spotify.remote.SpotifySource
import bilal.altify.domain.spotify.repositories.AltifyRepositories
import bilal.altify.domain.spotify.use_case.AltifyUseCases
import bilal.altify.domain.spotify.use_case.Command
import bilal.altify.domain.spotify.use_case.ContentCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val spotifySource: SpotifySource,
    private val useCases: AltifyUseCases,
    private val preferences: PreferencesRepository,
) : ViewModel() {

    val uiState = spotifySource.data
        .onEach {
            // ensures the browser is loaded asap
            when (it) {
                is SpotifyConnectorResponse.Connected ->
                    useCases.commands(
                        command = ContentCommand.GetRecommended,
                        repositories = it.repositories
                    )
                is SpotifyConnectorResponse.ConnectionFailed -> {}
            }
        }
        .flatMapLatest { response ->
            when (response) {
                is SpotifyConnectorResponse.Connected ->
                    useCases.browser(response.repositories)
                        .map(BrowserUIState::Success)
                is SpotifyConnectorResponse.ConnectionFailed ->
                    flowOf(BrowserUIState.Loading)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BrowserUIState.Loading
        )

}

sealed interface BrowserUIState {
    object Loading : BrowserUIState
    data class Success(
        val browserState: BrowserState,
    ) : BrowserUIState
}