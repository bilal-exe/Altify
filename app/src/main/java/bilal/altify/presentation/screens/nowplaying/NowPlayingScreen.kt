package bilal.altify.presentation.screens.nowplaying

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.data.dataclasses.AltPlayerContext
import bilal.altify.data.dataclasses.AltTrack
import bilal.altify.presentation.AltifyUIState
import bilal.altify.presentation.AltifyViewModel

@Composable
fun NowPlayingScreen(
    viewModel: AltifyViewModel,
    uiState: AltifyUIState,
    navToSettings: () -> Unit
) {
    val pauseResume = viewModel::pauseResume
    val skipPrevious = viewModel::skipPrevious
    val skipNext = viewModel::skipNext
    val play = viewModel::play
    val queue = viewModel::addToQueue
    val seek = viewModel::seek
    val increaseVolume = viewModel::increaseVolume
    val decreaseVolume = viewModel::decreaseVolume
    val setVolume = viewModel::setVolume

    NowPlayingScreen(
        uiState = uiState,
        navToSettings = navToSettings,
        pauseResume = pauseResume,
        skipPrevious = skipPrevious,
        skipNext = skipNext,
        play = play,
        queue = queue,
        seek = seek,
        increaseVolume = increaseVolume,
        decreaseVolume = decreaseVolume,
        setVolume = setVolume
    )
}

@Composable
fun NowPlayingScreen(
    uiState: AltifyUIState,
    navToSettings: () -> Unit,
    pauseResume: () -> Unit,
    skipPrevious: () -> Unit,
    skipNext: () -> Unit,
    play: (String) -> Unit,
    queue: (String) -> Unit,
    seek: (Long) -> Unit,
    increaseVolume: () -> Unit,
    decreaseVolume: () -> Unit,
    setVolume: (Float) -> Unit,
) {

    var showControls by remember { mutableStateOf(true) }
    val toggleControls = { showControls = !showControls }

    NowPlayingBackground(uiState.artwork) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            if (uiState.playerContext != null) NowPlayingTopBar(
                player = uiState.playerContext,
                rightButtonIcon = Icons.Default.Settings,
                onRightButtonClick = navToSettings
            )
            NowPlayingArtwork(uiState.artwork, toggleControls)
            NowPlayingMusicInfo(uiState.track)
            NowPlayingProgressBar(
                progress = uiState.playbackPosition,
                duration = uiState.track?.duration ?: 0,
                onSliderMoved = { seek(it) }
            )
            NowPlayingMusicControls(pauseResume, skipPrevious, skipNext)
            NowPlayingVolumeSlider(uiState.volume, increaseVolume, decreaseVolume, setVolume)
        }
    }
}

@Preview(name = "NowPlaying")
@Composable
private fun NowPlayingPreview() {
    NowPlayingScreen(
        uiState = AltifyUIState(
            playerContext = AltPlayerContext.example,
            track = AltTrack.example,
        ),
        navToSettings = {},
        pauseResume = { },
        skipPrevious = { },
        skipNext = { },
        play = { },
        queue = { },
        seek = { },
        increaseVolume = {},
        decreaseVolume = {},
        setVolume = {}
    )
}