package bilal.altify.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.domain.sources.SpotifyConnector
import bilal.altify.domain.sources.SpotifyConnectorResponse
import bilal.altify.domain.use_case.AltifyUseCases
import bilal.altify.domain.use_case.Command
import bilal.altify.domain.use_case.ContentCommand
import bilal.altify.presentation.prefrences.AltifyPreferencesDataSource
import bilal.altify.presentation.util.collectLatestOn
import bilal.altify.presentation.volume_notification.VolumeNotifications
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AltifyViewModel @Inject constructor(
    private val spotifyConnector: SpotifyConnector,
    private val preferences: AltifyPreferencesDataSource,
    private val volumeNotifications: VolumeNotifications,
    private val useCases: AltifyUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AltifyUIState())
    val uiState = _uiState.asStateFlow()

    init {
        connect()
        // interpolates playback position between Spotify callbacks
        uiState.collectLatestOn(viewModelScope) {
            while (uiState.value.connectionState is AltifyConnectionState.Success && !uiState.value.trackState.isPaused) {
                delay(INTERPOLATION_FREQUENCY_MS)
                _uiState.update {
                    uiState.value.copy(
                        trackState = uiState.value.trackState.copy(
                            playbackPosition = uiState.value.trackState.playbackPosition + INTERPOLATION_FREQUENCY_MS
                        )
                    )
                }
            }
        }
    }

    fun connect() {
        _uiState.update {
            it.copy(connectionState = AltifyConnectionState.Connecting)
        }
        spotifyConnector.connect().collectLatestOn(viewModelScope) { response ->
            when (response) {
                is SpotifyConnectorResponse.Connected -> {

                    combine(
                        preferences.state,
                        useCases.currentTrack(response.repositories),
                        useCases.browser(response.repositories)
                    ) { pref, track, browser ->
                        _uiState.update {
                            it.copy(
                                connectionState = AltifyConnectionState.Success(response.repositories),
                                preferences = pref,
                                trackState = track,
                                browserState = browser,
                            )
                        }
                    }.launchIn(
                        viewModelScope
                    )

                    useCases.commands(
                        command = ContentCommand.GetRecommended,
                        repositories = response.repositories
                    )
                    volumeNotifications.show(
                        scope = viewModelScope,
                        volume = uiState.map { it.trackState.volume }
                    )
                }

                is SpotifyConnectorResponse.ConnectionFailed -> {
                    _uiState.update {
                        it.copy(
                            connectionState = AltifyConnectionState.Disconnected(
                                response.exception.localizedMessage
                            )
                        )
                    }
                    volumeNotifications.delete()
                }
            }
        }
    }

    fun executeCommand(command: Command) {
        if (uiState.value.connectionState !is AltifyConnectionState.Success) return
        useCases.commands(
            command = command,
            repositories = (uiState.value.connectionState as AltifyConnectionState.Success).sources,
        )
    }

    override fun onCleared() {
        volumeNotifications.delete()
        super.onCleared()
    }

    companion object {
        const val INTERPOLATION_FREQUENCY_MS = 500L
    }
}