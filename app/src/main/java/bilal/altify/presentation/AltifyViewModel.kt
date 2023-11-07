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
import bilal.altify.domain.spotify.use_case.model.Command
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
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
        if (
            spotifyConnector is SpotifyConnectorResponse.Connected &&
            token is TokenState.Token &&
            token.expiry > Instant.now()
        )
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
            val error = when (token) {
                TokenState.Empty -> APITokenError.EMPTY
                is TokenState.Token -> APITokenError.EXPIRED
            }
            AltifyUIState.Disconnected(Error.APIToken(error))
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AltifyUIState.Connecting
        )

    // automatically refresh token on expiry
    val refreshTokenSignalFlow = accessTokenRepository
        .state
        .distinctUntilChanged()
        .mapLatest {
            when (it) {
                is TokenState.Token -> {
                    val timeToWait = it.expiry.epochSecond - (Instant.now().epochSecond - 5)
                    if (timeToWait > 0) delay(Duration.ofSeconds(timeToWait).toMillis())
                    true
                }
                else -> false
            }
        }
        .filter { it }

    private val _errors = MutableStateFlow<String?>(null)
    val errors = _errors.asStateFlow()

    fun connect() =
        spotifyConnector.connect()

    fun executeCommand(command: Command, repositories: AltifyRepositories) =
        useCases.commands(
            command = command,
            repositories = repositories,
        )

    fun onAuthorizationResponse(response: AuthorizationResponse) {
        viewModelScope.launch {
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    val token = TokenState.Token(
                        accessToken = response.accessToken,
                        expiry = Instant.now().plusSeconds(response.expiresIn.toLong())
                    )
                    accessTokenRepository.setToken(token)
                }
                AuthorizationResponse.Type.ERROR ->
                    _errors.emit(response.error ?: "Could not get a Spotify API token")
                else ->
                    _errors.emit(response.error ?: "There was a problem getting the Spotify API token")
            }
        }
    }

}