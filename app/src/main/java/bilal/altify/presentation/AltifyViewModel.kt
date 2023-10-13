package bilal.altify.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.domain.spotify.remote.appremote.SpotifyConnectorResponse
import bilal.altify.domain.spotify.use_case.AltifyUseCases
import bilal.altify.domain.spotify.use_case.Command
import bilal.altify.domain.prefrences.PreferencesRepository
import bilal.altify.domain.spotify.repositories.AltifyRepositories
import bilal.altify.domain.spotify.remote.appremote.SpotifyConnector
import bilal.altify.domain.spotify.remote.web_api.AccessTokenRepository
import bilal.altify.domain.spotify.remote.web_api.TokenState
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class AltifyViewModel @Inject constructor(
    private val spotifyConnector: SpotifyConnector,
    private val preferences: PreferencesRepository,
    private val accessTokenRepository: AccessTokenRepository,
    private val useCases: AltifyUseCases
) : ViewModel() {

    val uiState = combine(
        spotifyConnector.data,
        preferences.state,
        accessTokenRepository.state
    ) { spotifyConnector, preferences, token ->
        if (spotifyConnector is SpotifyConnectorResponse.Connected && token is TokenState.Token)
            AltifyUIState.Success(
                preferences = preferences,
                repositories = spotifyConnector.repositories,
                token = token.accessToken
            )
        else if (spotifyConnector is SpotifyConnectorResponse.ConnectionFailed)
            AltifyUIState.Disconnected(
                Error.SpotifyConnector(spotifyConnector.exception.localizedMessage)
            )
        else {
            AltifyUIState.Disconnected(Error.APIToken)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AltifyUIState.Connecting
        )

    private fun refreshToken() {

    }

    fun connect() =
        spotifyConnector.connect()

    fun executeCommand(command: Command, repositories: AltifyRepositories) =
        useCases.commands(
            command = command,
            repositories = repositories,
        )

    fun onAuthorizationResponse(response: AuthorizationResponse?) {
        if (response == null) return
        when (response.type) {
            AuthorizationResponse.Type.TOKEN -> TODO()
//                viewModelScope.launch { preferences.setSpotifyToken(response.accessToken) }
            AuthorizationResponse.Type.ERROR -> TODO()
            else -> TODO()
        }
    }
}