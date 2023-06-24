package bilal.altify.presentation

import bilal.altify.data.spotify.SpotifyController
import bilal.altify.data.spotify.toAlt
import bilal.altify.presentation.prefrences.AltifyPreferencesDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update

private class Collector(
    val scope: CoroutineScope, val block: suspend (SpotifyController) -> Unit
) {

    private var job: Job? = null

    fun start(controller: SpotifyController) {
        job = scope.launch { block(controller) }
    }

    fun stop() {
        job?.cancel()
        job = null
    }

}

class StateUpdater(
    private val uiState: MutableStateFlow<AltifyUIState>,
    private val preferences: AltifyPreferencesDataSource,
    private val scope: CoroutineScope
) {

    private val preferencesCollector = Collector(scope) {
        preferences.state.collect { preferences ->
            uiState.update {
                (it as AltifyUIState.Connected).copy(preferences = preferences)
            }
        }
    }

    private val trackCollector = Collector(scope) { controller ->
        controller.player.currentTrack.collect { track ->
            val alt = track?.toAlt()
            if (alt != null && alt != (uiState.value as AltifyUIState.Connected).track) {
                alt.imageUri?.let { controller.image.getLargeImage(it) }
            }
            uiState.update { (it as AltifyUIState.Connected).copy(track = alt) }
        }
    }

    private val positionInterpolator = Collector(scope) {
        while (true) {
            delay(100)
            uiState.update {
                (it as AltifyUIState.Connected).copy(playbackPosition = it.playbackPosition + 100)
            }
        }
    }

    private val isPausedCollector = Collector(scope) { controller ->
        controller.player.isPaused.collect { isPaused ->
            uiState.update { (it as AltifyUIState.Connected).copy(isPaused = isPaused) }
            if (isPaused) positionInterpolator.stop()
            else positionInterpolator.start(controller)
        }
    }

    private val playbackPositionCollector = Collector(scope) { controller ->
        controller.player.playbackPosition.collect { pos ->
            uiState.update { (it as AltifyUIState.Connected).copy(playbackPosition = pos) }
        }
    }

    private val playerContextCollector = Collector(scope) { controller ->
        controller.player.playerContext.collect { playerContext ->
            uiState.update { (it as AltifyUIState.Connected).copy(playerContext = playerContext?.toAlt()) }
        }
    }

    private val listItemsCollector = Collector(scope) { controller ->
        controller.content.listItemsFlow.collect { listItems ->
            uiState.update { oldState ->
                (oldState as AltifyUIState.Connected).copy(
                    listItems = listItems.items.map { it.toAlt() }.toTypedArray()
                )
            }
        }
    }

    private val volumeCollector = Collector(scope) { controller ->
        controller.volume.volume.collect { vol ->
            if (vol != null) uiState.update { (it as AltifyUIState.Connected).copy(volume = vol.mVolume) }
        }
    }

    private val artworkCollector = Collector(scope) { controller ->
        controller.image.artwork.collect { artwork ->
            uiState.update { (it as AltifyUIState.Connected).copy(artwork = artwork) }
        }
    }


    private val collectors = listOf(
        trackCollector,
        isPausedCollector,
        playbackPositionCollector,
        playerContextCollector,
        listItemsCollector,
        volumeCollector,
        artworkCollector,
    )

    fun onConnected(controller: SpotifyController) {
        collectors.forEach { it.start(controller) }
    }

    fun onDisconnected() {
        collectors.forEach { it.stop() }
    }

}