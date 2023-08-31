package bilal.altify.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.domain.controller.AltifyRepositories
import bilal.altify.domain.model.AltPlayerStateAndContext.Companion.INTERPOLATION_FREQUENCY_MS
import bilal.altify.domain.repository.SpotifyConnector
import bilal.altify.domain.repository.SpotifyConnectorResponse
import bilal.altify.presentation.prefrences.AltifyPreferencesDataSource
import bilal.altify.presentation.volume_notification.VolumeNotifications
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AltifyViewModel @Inject constructor(
    private val spotifyConnector: SpotifyConnector,
    private val preferences: AltifyPreferencesDataSource,
    private val volumeNotifications: VolumeNotifications
) : ViewModel() {

    private val _uiState = MutableStateFlow(AltifyUIState())
    val uiState = _uiState.asStateFlow()

    private var repositories: AltifyRepositories? = null

    init {
        connect()
        var latestImageUri: String? = ""
        viewModelScope.launch {
            uiState.collect {
                if (it.track?.imageUri != latestImageUri) {
                    latestImageUri = it.track?.imageUri
                    it.track?.imageUri?.let { it1 -> repositories?.images?.getArtwork(it1) }
                }
            }
        }
        viewModelScope.launch {
            uiState.collectLatest {
                while (uiState.value.connectionState is AltifyConnectionState.Success && !uiState.value.isPaused) {
                    delay(INTERPOLATION_FREQUENCY_MS)
                    _uiState.update { uiState.value.copy(playbackPosition = uiState.value.playbackPosition + INTERPOLATION_FREQUENCY_MS) }
                }
            }
        }
    }


    fun connect() {
        _uiState.update { it.copy(connectionState = AltifyConnectionState.Connecting) }
        viewModelScope.launch {
            spotifyConnector.connect().collectLatest { response ->

                when (response) {
                    is SpotifyConnectorResponse.Connected -> {

                        repositories = response.repositories

                        combine(
                            preferences.state,
                            repositories!!.player.getPlayerStateAndContext(),
                            repositories!!.volume.getVolume(),
                            repositories!!.content.getListItemsFlow(),
                            repositories!!.images.getArtworkFlow()
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

                        volumeNotifications.show(viewModelScope, uiState.map { it.volume })
                    }

                    is SpotifyConnectorResponse.ConnectionFailed -> {
                        _uiState.update {
                            it.copy(
                                connectionState = AltifyConnectionState.Disconnected(
                                    response.exception.localizedMessage
                                )
                            )
                        }
                        repositories = null

                        volumeNotifications.delete()
                    }

                }
            }
        }
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
                repositories?.volume?.increaseVolume()
            }
            is VolumeCommand.SetVolume -> {
                repositories?.volume?.setVolume(command.volume)
            }

            // images
            is ImagesCommand.GetThumbnail -> TODO()
        }

    }

    override fun onCleared() {
        volumeNotifications.delete()
        super.onCleared()
    }
}