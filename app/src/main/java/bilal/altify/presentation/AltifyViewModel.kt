package bilal.altify.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.data.spotify.SpotifyController
import com.spotify.protocol.types.ListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AltifyViewModel @Inject constructor(
    private val spotifyControllerFactory: SpotifyController.SpotifyControllerFactory
) : ViewModel() {

    private val _uiState = MutableStateFlow<AltifyUIState>(AltifyUIState.Connecting)
    val uiState = _uiState.asStateFlow()

    private var controller: SpotifyController? = null
    val stateUpdater = StateUpdater(_uiState, viewModelScope)

    init {
        connect()
    }

    fun connect() {
        _uiState.value = AltifyUIState.Connecting
        viewModelScope.launch {
            spotifyControllerFactory.connect().fold(
                onSuccess = { onConnected(it) },
                onFailure = { onDisconnected(it) }
            )
        }
    }

    private fun onConnected(spotifyController: SpotifyController) {
        controller = spotifyController
        _uiState.value = AltifyUIState.Connected()
        stateUpdater.onConnected(spotifyController)
    }

    private fun onDisconnected(throwable: Throwable) {
        controller = null
        _uiState.value = AltifyUIState.Disconnected(throwable.message)
        stateUpdater.onDisconnected()
    }

    fun pauseResume() {
        if (controller == null) return
        viewModelScope.launch { controller!!.player.pauseResume() }
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