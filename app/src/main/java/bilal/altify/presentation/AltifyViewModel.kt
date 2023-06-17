package bilal.altify.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.data.SpotifyController
import com.spotify.protocol.types.ListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AltifyViewModel @Inject constructor(
    private val spotifyControllerFactory: SpotifyController.SpotifyControllerFactory
) : ViewModel() {

    private val _uiState = MutableStateFlow<AltifyUIState>(AltifyUIState.Connecting)
    val uiState = _uiState.asStateFlow()

    private var controller: SpotifyController? = null
    private val collectionJobs = mutableListOf<Job?>()

    init {
        connect()
    }

    fun connect() {
        _uiState.value = AltifyUIState.Connecting
        viewModelScope.launch {
            spotifyControllerFactory.connect().fold(
                onSuccess = { onConnected(it) },
                onFailure = { onConnectionFailed(it) }
            )
        }
    }

    private fun onConnected(spotifyController: SpotifyController) {
        controller = spotifyController
        _uiState.value = AltifyUIState.Connected()
        collectionBlocks.forEach { collectionJobs += viewModelScope.launch { it() } }
    }

    private fun onConnectionFailed(throwable: Throwable) {
        controller = null
        _uiState.value = AltifyUIState.Disconnected(throwable.message)
        collectionJobs.forEach { it?.cancel() }
        collectionJobs.clear()
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

    fun getRecommendedListItems() {
        if (controller == null) return
        viewModelScope.launch { controller!!.content.getRecommended() }
    }

    fun getChildItems(listItem: ListItem) {
        if (controller == null) return
        viewModelScope.launch { controller!!.content.getChildrenOfItem(listItem) }
    }

    fun play(listItem: ListItem) {
        if (controller == null) return
        viewModelScope.launch { controller!!.content.play(listItem) }
    }

    private val collectionBlocks = arrayOf<suspend CoroutineScope.() -> Unit>(
        {
            controller!!.player.currentTrack.collect { track ->
                _uiState.update { (it as AltifyUIState.Connected).copy(track = track) }
            }
        },
        {
            controller!!.player.isPaused.collect { isPaused ->
                _uiState.update { (it as AltifyUIState.Connected).copy(isPaused = isPaused) }
            }
        },
        {
            controller!!.player.playbackPosition.collect { pos ->
                _uiState.update { (it as AltifyUIState.Connected).playbackPosition = pos; it }
            }
        },
        {
            controller!!.player.playerContext.collect { playerContext ->
                _uiState.update { (it as AltifyUIState.Connected).copy(playerContext = playerContext) }
            }
        },
        {
            controller!!.content.listItemsFlow.collect { listItems ->
                _uiState.update { (it as AltifyUIState.Connected).copy(listItems = listItems.items) }
            }
        },
        {
            controller!!.volume.volume.collect { vol ->
                _uiState.update { (it as AltifyUIState.Connected).copy(volume = vol!!.mVolume) }
            }
        }
    )
}