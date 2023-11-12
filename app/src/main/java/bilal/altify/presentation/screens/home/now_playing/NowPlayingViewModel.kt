package bilal.altify.presentation.screens.home.now_playing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.domain.prefrences.PreferencesRepository
import bilal.altify.domain.spotify.use_case.model.CurrentTrackState
import bilal.altify.domain.spotify.repositories.SpotifyConnectorResponse
import bilal.altify.domain.spotify.repositories.SpotifySource
import bilal.altify.domain.spotify.use_case.AltifyUseCases
import bilal.altify.presentation.prefrences.AltPreferencesState
import bilal.altify.presentation.volume_notification.VolumeNotifications
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val spotifySource: SpotifySource,
    private val useCases: AltifyUseCases,
    private val volumeNotifications: VolumeNotifications,
    private val preferences: PreferencesRepository,
) : ViewModel() {

    val uiState = spotifySource.data
        .flatMapLatest {
            when (it) {
                is SpotifyConnectorResponse.Connected ->
                        combine(
                            useCases.currentTrack(it.repositories),
                            preferences.state,
                            NowPlayingUIState::Success
                        )
                is SpotifyConnectorResponse.ConnectionFailed ->
                    flowOf(NowPlayingUIState.Loading)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NowPlayingUIState.Loading
        )


    init {
        val success = uiState
            .filter { it is NowPlayingUIState.Success } as Flow<NowPlayingUIState.Success>
        volumeNotifications.show(
            scope = viewModelScope,
            volume =  success.map { it.trackState.volume }
        )
    }

    override fun onCleared() {
        volumeNotifications.delete()
        super.onCleared()
    }
}

sealed interface NowPlayingUIState {
    object Loading : NowPlayingUIState
    data class Success(
        val trackState: CurrentTrackState,
        val preferences: AltPreferencesState,
    ) : NowPlayingUIState
}