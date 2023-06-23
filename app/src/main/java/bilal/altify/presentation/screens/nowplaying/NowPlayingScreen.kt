package bilal.altify.presentation.screens.nowplaying

import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import bilal.altify.data.dataclasses.AltPlayerContext
import bilal.altify.data.dataclasses.AltTrack
import bilal.altify.presentation.AltifyUIState
import bilal.altify.presentation.AltifyViewModel

@Composable
fun NowPlayingScreen(viewModel: AltifyViewModel, uiState: AltifyUIState.Connected) {
    val pauseResume = viewModel::pauseResume
    val skipPrevious = viewModel::skipPrevious
    val skipNext = viewModel::skipNext
    val play = viewModel::play
    val queue = viewModel::addToQueue
    val seek = viewModel::seek
    val getArtwork = viewModel::getLargeImage
    val increaseVolume = viewModel::increaseVolume
    val decreaseVolume = viewModel::decreaseVolume
    val setVolume = viewModel::setVolume
    val coverArt by remember { viewModel.largeImage }

    NowPlayingScreen(
        uiState,
        pauseResume,
        skipPrevious,
        skipNext,
        play,
        queue,
        seek,
        coverArt,
        increaseVolume,
        decreaseVolume,
        setVolume
    )
}

@Composable
fun NowPlayingScreen(
    uiState: AltifyUIState.Connected,
    pauseResume: () -> Unit,
    skipPrevious: () -> Unit,
    skipNext: () -> Unit,
    play: (String) -> Unit,
    queue: (String) -> Unit,
    seek: (Long) -> Unit,
    coverArt: Bitmap?,
    increaseVolume: () -> Unit,
    decreaseVolume: () -> Unit,
    setVolume: (Float) -> Unit,
) {

    var showControls by remember { mutableStateOf(true) }
    val toggleControls = { showControls = !showControls }

    NowPlayingBackground(coverArt) {
        if (uiState.playerContext != null) NowPlayingTopBar(player = uiState.playerContext)
        NowPlayingArtwork(coverArt, toggleControls)
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

@Preview(name = "NowPlaying")
@Composable
private fun NowPlayingPreview() {
    NowPlayingScreen(
        uiState = AltifyUIState.Connected(
            playerContext = AltPlayerContext.example,
            track = AltTrack.example,
        ),
        pauseResume = { },
        skipPrevious = { },
        skipNext = { },
        play = { },
        queue = { },
        seek = { },
        coverArt = null,
        increaseVolume = {},
        decreaseVolume = {},
        setVolume = {}
    )
}
