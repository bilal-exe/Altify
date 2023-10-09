package bilal.altify.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.domain.spotify.repositories.SpotifySource
import bilal.altify.domain.spotify.repositories.SpotifyConnectorResponse
import bilal.altify.domain.spotify.use_case.AltifyUseCases
import bilal.altify.domain.spotify.use_case.Command
import bilal.altify.data.prefrences.DatastorePreferencesDataSource
import bilal.altify.domain.prefrences.PreferencesRepository
import bilal.altify.domain.spotify.repositories.AltifyRepositories
import bilal.altify.domain.spotify.repositories.SpotifyConnector
import bilal.altify.domain.spotify.use_case.ContentCommand
import bilal.altify.presentation.util.collectLatestOn
import bilal.altify.presentation.volume_notification.VolumeNotifications
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class AltifyViewModel @Inject constructor(
    private val spotifyConnector: SpotifyConnector,
    private val preferences: PreferencesRepository,
    private val useCases: AltifyUseCases
) : ViewModel() {

    val uiState = spotifyConnector.data
        .flatMapLatest { response ->
            when (response) {
                is SpotifyConnectorResponse.Connected ->
                    combine(
                        preferences.state,
                        flowOf(response.repositories),
                        AltifyUIState::Success
                    )
                is SpotifyConnectorResponse.ConnectionFailed ->
                    flowOf(AltifyUIState.Disconnected(response.exception.message))

            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AltifyUIState.Connecting
        )

    fun connect() =
        spotifyConnector.connect()

    fun executeCommand(command: Command, repositories: AltifyRepositories) =
        useCases.commands(
            command = command,
            repositories = repositories,
        )
}