package bilal.altify.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.domain.controller.AltifyRepositories
import bilal.altify.domain.repository.SpotifyConnector
import bilal.altify.domain.repository.SpotifyConnectorResponse
import bilal.altify.presentation.prefrences.AltifyPreferencesDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AltifyViewModel @Inject constructor(
    private val spotifyConnector: SpotifyConnector,
    private val preferences: AltifyPreferencesDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(AltifyUIState())
    val uiState = _uiState.asStateFlow()

    var repositories: AltifyRepositories? = null

    init {
        viewModelScope.launch {
            spotifyConnector.spotifyConnectorFlow.collectLatest { response ->

                when (response) {
                    is SpotifyConnectorResponse.Connected -> {

                        repositories = response.repositories

                        // would i have to create a job var and cancel?
                        combine(
                            preferences.state,
                            repositories!!.player.getPlayerState(),
                            repositories!!.volume.getVolume(),
                            repositories!!.content.getListItems(),
                            repositories!!.images.getArtwork()
                        ) { pref, player, vol, content, images ->
                            _uiState.update {
                                it.copy(
                                    connectionState = AltifyConnectionState.Success,
                                    preferences = pref,
                                    track = player.track,
                                    isPaused = player.isPaused,
                                    playbackPosition = player.position,
                                    playerContext = player.context,
                                    volume = vol,
                                    listItems = content,
                                    artwork = images
                                )
                            }
                        }.launchIn(
                            viewModelScope
                        )
                    }

                    is SpotifyConnectorResponse.ConnectionFailed -> {
                        _uiState.update {
                            it.copy(connectionState = AltifyConnectionState.Disconnected(
                                    response.exception.localizedMessage
                                )
                            )
                        }
                        repositories = null
                    }

                }
            }
        }
        var latestImageUri: String? = ""
        viewModelScope.launch {
            uiState.collect {
                if (it.track?.imageUri != latestImageUri) {
                    latestImageUri = it.track?.imageUri
                    it.track?.imageUri?.let { it1 -> repositories?.images?.getArtwork(it1) }
                }
            }
        }
    }


    fun connect() {
        _uiState.update { it.copy(connectionState = AltifyConnectionState.Connecting) }
        spotifyConnector.connect()
    }

    fun executeCommand(command: Command) {

        if (repositories == null) throw Exception()

        when (command) {

            // playback
            is PlaybackCommand.PauseResume -> {
                repositories?.player?.pauseResume(command.isPaused)
            }
            PlaybackCommand.SkipPrevious -> {
                repositories?.player?.skipPrevious()
            }
            PlaybackCommand.SkipNext -> {
                repositories?.player?.skipNext()
            }
            is PlaybackCommand.Play -> {
                repositories?.player?.play(command.uri)
            }
            is PlaybackCommand.Seek -> {
                repositories?.player?.seek(command.position)
            }
            is PlaybackCommand.AddToQueue -> {
                repositories?.player?.addToQueue(command.uri)
            }
            is PlaybackCommand.SkipToTrack -> {
                repositories?.player?.skipToTrack(command.uri, command.index)
            }

            // content
            ContentCommand.GetRecommended -> {
                repositories?.content?.getRecommended()
            }
            is ContentCommand.GetChildrenOfItem -> {
                repositories?.content?.getChildrenOfItem(command.listItem)
            }
            is ContentCommand.Play -> {
                repositories?.content?.play(command.listItem)
            }

            //volume
            VolumeCommand.DecreaseVolume -> {
                repositories?.volume?.decreaseVolume()
            }
            VolumeCommand.IncreaseVolume -> {
                repositories?.volume?.decreaseVolume()
            }
            is VolumeCommand.SetVolume -> {
                repositories?.volume?.setVolume(command.volume)
            }
        }

    }
}