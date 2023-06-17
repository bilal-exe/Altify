package bilal.altify.presentation.screens.nowplaying

import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import bilal.altify.presentation.AltifyUIState
import bilal.altify.presentation.AltifyViewModel
import com.spotify.protocol.types.PlayerContext

@Composable
fun NowPlayingScreen(viewModel: AltifyViewModel, uiState: AltifyUIState.Connected) {
    val pauseResume = viewModel::pauseResume
    val skipPrevious = viewModel::skipPrevious
    val skipNext = viewModel::skipNext
    val play = viewModel::play
    val queue = viewModel::addToQueue
    val seek = viewModel::seek
    val getArtwork = viewModel::getLargeImage

    var coverArt by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(key1 = uiState.track?.imageUri) {
        coverArt = uiState.track ?.let { getArtwork(it.imageUri) }
    }

    NowPlayingScreen(uiState, pauseResume, skipPrevious, skipNext, play, queue, seek, coverArt)
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
    coverArt: Bitmap?
) {

    NowPlayingBackground(coverArt) {
        if (uiState.playerContext != null) NowPlayingTopBar(player = uiState.playerContext)
        NowPlayingArtwork(coverArt)
        TODO()
    }
}

@Preview(name = "NowPlaying")
@Composable
private fun NowPlayingPreview() {
    val playerContext = PlayerContext("", "Title", "Subtitle", "Type")
    NowPlayingScreen(
        uiState = AltifyUIState.Connected(playerContext = playerContext),
        pauseResume = { },
        skipPrevious = { },
        skipNext = { },
        play = { },
        queue = { },
        seek = { },
        coverArt = null
    )
}
