package bilal.altify.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.domain.prefrences.PreferencesRepository
import bilal.altify.domain.spotify.remote.appremote.SpotifyConnector
import bilal.altify.domain.spotify.remote.appremote.SpotifyConnectorResponse
import bilal.altify.domain.spotify.remote.web_api.AccessTokenRepository
import bilal.altify.domain.spotify.remote.web_api.TokenState
import bilal.altify.domain.spotify.repositories.AltifyRepositories
import bilal.altify.domain.spotify.use_case.AltifyUseCases
import bilal.altify.domain.spotify.use_case.Command
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
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
            AuthorizationResponse.Type.TOKEN ->
                viewModelScope.launch {
                    val token = TokenState.Token(
                        accessToken = response.accessToken,
                        expiry = Instant.now().plusSeconds(response.expiresIn.toLong())
                    )
                    accessTokenRepository.setToken(token)
                }
            AuthorizationResponse.Type.ERROR -> TODO()
            else -> TODO()
        }
    }
}