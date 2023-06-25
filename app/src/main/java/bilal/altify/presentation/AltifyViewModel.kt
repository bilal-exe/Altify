package bilal.altify.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.data.spotify.SpotifyController
import bilal.altify.presentation.prefrences.AltifyPreferencesDataSource
import com.spotify.protocol.types.ListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AltifyViewModel @Inject constructor(
    private val spotifyControllerFactory: SpotifyController.SpotifyControllerFactory,
    private val preferences: AltifyPreferencesDataSource
) : ViewModel() {

    private var controller: SpotifyController? = null
//    val stateUpdater = StateUpdater(_uiState, preferences, viewModelScope)

    private val _uiState = MutableStateFlow<AltifyUIState>(AltifyUIState.Connecting)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            spotifyControllerFactory.connectionsFlow.collect { result ->

                val onSuccessBlocks = arrayOf<suspend (SpotifyController) -> Unit>(
                    { sc ->
                        while (true) {
                            delay(2500)
                            if (!sc.isConnected()) connect()
                        }
                    },
                    { sc -> collectSpotifyInfo(sc).collect { new -> _uiState.update { new } } }
                )
                var onSuccessJobs = emptyList<Job>()

                result.fold(
                    onSuccess = { sc ->
                        controller = sc
                        onSuccessJobs = onSuccessBlocks.map { viewModelScope.launch { it(sc) } }
                    },
                    onFailure = { exception ->
                        onSuccessJobs.forEach { it.cancel() }
                        onSuccessJobs = emptyList()
                        controller = null
                        _uiState.value = AltifyUIState.Disconnected(exception.message)
                    }
                )

            }
        }
    }

    private fun collectSpotifyInfo(controller: SpotifyController): Flow<AltifyUIState.Connected> =
        combine(
            preferences.state,
            controller.player.altPlayerState,
            controller.volume.volume,
            controller.content.listItemsFlow,
            controller.image.artwork
        ) { pref, ps, vol, li, art ->
            ps.track?.imageUri?.let { controller.image.getLargeImage(it) }
            AltifyUIState.Connected(
                preferences = pref,
                track = ps.track,
                isPaused = ps.isPaused,
                playbackPosition = ps.position,
                playerContext = ps.context,
                volume = vol,
                listItems = li,
                artwork = art
            )
        }


    fun connect() {
        _uiState.value = AltifyUIState.Connecting
        spotifyControllerFactory.connect()
    }

    fun pauseResume() {
        if (controller == null) return
        viewModelScope.launch { controller!!.player.pauseResume((uiState.value as AltifyUIState.Connected).isPaused) }
    }

    fun skipNext() {
        if (controller == null) return
        viewModelScope.launch { controller!!.player.skipNext() }
    }

    fun skipPrevious() {
        if (controller == null) return
        viewModelScope.launch { controller!!.player.skipPrevious() }
    }

    fun play(uri: String) {
        if (controller == null) return
        viewModelScope.launch { controller!!.player.play(uri) }
    }

    fun seek(to: Long) {
        if (controller == null) return
        viewModelScope.launch { controller!!.player.seek(to) }
    }

    fun addToQueue(uri: String) {
        if (controller == null) return
        viewModelScope.launch { controller!!.player.addToQueue(uri) }
    }

    fun increaseVolume() {
        if (controller == null) return
        viewModelScope.launch { controller!!.volume.increaseVolume() }
    }

    fun decreaseVolume() {
        if (controller == null) return
        viewModelScope.launch { controller!!.volume.decreaseVolume() }
    }

    fun setVolume(volume: Float) {
        if (controller == null) return
        viewModelScope.launch { controller!!.volume.setVolume(volume) }
    }

    fun getRecommendedListItems() {
        if (controller == null) return
        viewModelScope.launch { controller!!.content.getRecommended() }
    }

    fun getChildItems(listItem: ListItem) {
        if (controller == null) return
        viewModelScope.launch { controller!!.content.getChildrenOfItem(listItem) }
    }

    fun playListItem(listItem: ListItem) {
        if (controller == null) return
        viewModelScope.launch { controller!!.content.play(listItem) }
    }
}