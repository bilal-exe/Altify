package bilal.altify.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.domain.spotify.remote.SpotifyConnectorResponse
import bilal.altify.domain.spotify.use_case.AltifyUseCases
import bilal.altify.domain.spotify.use_case.Command
import bilal.altify.domain.prefrences.PreferencesRepository
import bilal.altify.domain.spotify.repositories.AltifyRepositories
import bilal.altify.domain.spotify.remote.SpotifyConnector
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    fun setSpotifyToken(accessToken: String?) {

    }

    fun onAuthorizationResponse(response: AuthorizationResponse?) {
        if (response == null) return
        when (response.type) {
            AuthorizationResponse.Type.TOKEN ->
                viewModelScope.launch { preferences.setSpotifyToken(response.accessToken) }
            AuthorizationResponse.Type.ERROR -> TODO()
            else -> TODO()
        }
    }
}