package bilal.altify.presentation.screens.home.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.data.spotify.respoitories.TracksRepositoryImpl
import bilal.altify.data.spotify.sources.TrackNetworkSource
import bilal.altify.domain.model.AudioFeatures
import bilal.altify.domain.model.Item
import bilal.altify.domain.model.TokenState
import bilal.altify.domain.prefrences.PreferencesRepository
import bilal.altify.domain.spotify.use_case.model.BrowserState
import bilal.altify.domain.spotify.repositories.SpotifyConnectorResponse
import bilal.altify.domain.spotify.repositories.SpotifySource
import bilal.altify.domain.spotify.repositories.TracksRepository
import bilal.altify.domain.spotify.use_case.AltifyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val spotifySource: SpotifySource,
    private val useCases: AltifyUseCases,
    private val preferences: PreferencesRepository,
    private val tracksRepository: TracksRepository
) : ViewModel() {

    val uiState = spotifySource.data
        .flatMapLatest { response ->
            when (response) {
                is SpotifyConnectorResponse.Connected ->
                    merge(
                        useCases.browser(response.repositories).map(BrowserUIState::Success),
                        audioFeaturesChannel.receiveAsFlow()
                    )
                is SpotifyConnectorResponse.ConnectionFailed ->
                    flowOf(BrowserUIState.Loading)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BrowserUIState.Loading
        )

    private val audioFeaturesChannel = Channel<BrowserUIState.ShowAudioFeatures>()

    fun getAudioFeatures(tokenState: TokenState.Token, track: Item.Track) {
        viewModelScope.launch {
            audioFeaturesChannel.send(
                BrowserUIState.ShowAudioFeatures(
                    audioFeatures = tracksRepository.getAudioFeatures(tokenState.accessToken, track.remoteId),
                    track = track
                )
            )
        }
    }

}

sealed interface BrowserUIState {
    object Loading : BrowserUIState
    data class Success(
        val browserState: BrowserState,
    ) : BrowserUIState
    data class ShowAudioFeatures(
        val audioFeatures: AudioFeatures,
        val track: Item.Track
    ) : BrowserUIState
}