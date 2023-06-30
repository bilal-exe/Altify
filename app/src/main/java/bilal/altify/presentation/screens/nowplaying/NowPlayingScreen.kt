package bilal.altify.presentation.screens.nowplaying

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.data.dataclasses.AltPlayerContext
import bilal.altify.data.dataclasses.AltTrack
import bilal.altify.presentation.AltifyUIState
import bilal.altify.presentation.AltifyViewModel
import bilal.altify.presentation.DarkThemeConfig

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
private fun NowPlayingScreen(
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
    val darkTheme = when (uiState.preferences.darkTheme) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }

    var showControls by remember { mutableStateOf(true) }
    val toggleControls = { showControls = !showControls }

    NowPlayingBackground(
        bitmap = uiState.artwork,
        darkTheme = darkTheme,
        style = uiState.preferences.backgroundStyle
    ) {
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AnimatedVisibility(
                    visible = showControls,
                    enter = slideInVertically { -it * 2 },
                    exit = slideOutVertically { -it * 2 }
                ) {
                    if (uiState.playerContext != null) NowPlayingTopBar(
                        player = uiState.playerContext,
                        rightButtonIcon = Icons.Default.Settings,
                        onRightButtonClick = navToSettings
                    )
                }
            },
        ) { paddingValues ->
            if (LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE)
                NowPlayingLandscapeContent(
                    paddingValues = paddingValues,
                    uiState = uiState,
                    pauseResume = pauseResume,
                    skipPrevious = skipPrevious,
                    skipNext = skipNext,
                    play = play,
                    queue = queue,
                    seek = seek,
                    increaseVolume = increaseVolume,
                    decreaseVolume = decreaseVolume,
                    setVolume = setVolume,
                    showControls = showControls,
                    toggleControls = toggleControls
                )
            else NowPlayingPortraitContent(
                paddingValues = paddingValues,
                uiState = uiState,
                pauseResume = pauseResume,
                skipPrevious = skipPrevious,
                skipNext = skipNext,
                play = play,
                queue = queue,
                seek = seek,
                increaseVolume = increaseVolume,
                decreaseVolume = decreaseVolume,
                setVolume = setVolume,
                showControls = showControls,
                toggleControls = toggleControls
            )
        }
    }
}

@Composable
private fun NowPlayingPortraitContent(
    paddingValues: PaddingValues,
    uiState: AltifyUIState,
    pauseResume: () -> Unit,
    skipPrevious: () -> Unit,
    skipNext: () -> Unit,
    play: (String) -> Unit,
    queue: (String) -> Unit,
    seek: (Long) -> Unit,
    increaseVolume: () -> Unit,
    showControls: Boolean,
    decreaseVolume: () -> Unit,
    setVolume: (Float) -> Unit,
    toggleControls: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp - paddingValues.calculateTopPadding()),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        NowPlayingArtwork(
            uiState.artwork,
            toggleControls,
            uiState.preferences.artworkDisplayConfig,
            uiState.isPaused,
            uiState.playbackPosition,
            skipPrevious = skipPrevious,
            skipNext = skipNext,
        )
        NowPlayingMusicInfo(uiState.track)
        AnimatedVisibility(
            visible = showControls,
            enter = slideInVertically { it * 2 },
            exit = slideOutVertically { it * 2 }
        ) {
            Column {
                NowPlayingProgressBar(
                    progress = uiState.playbackPosition,
                    duration = uiState.track?.duration ?: 0,
                    onSliderMoved = { seek(it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                NowPlayingMusicControls(pauseResume, skipPrevious, skipNext)
                Spacer(modifier = Modifier.height(16.dp))
                NowPlayingVolumeSlider(
                    uiState.volume,
                    increaseVolume,
                    decreaseVolume,
                    setVolume
                )
            }
        }
    }
}

@Composable
private fun NowPlayingLandscapeContent(
    paddingValues: PaddingValues,
    uiState: AltifyUIState,
    pauseResume: () -> Unit,
    skipPrevious: () -> Unit,
    skipNext: () -> Unit,
    play: (String) -> Unit,
    queue: (String) -> Unit,
    seek: (Long) -> Unit,
    increaseVolume: () -> Unit,
    decreaseVolume: () -> Unit,
    setVolume: (Float) -> Unit,
    showControls: Boolean,
    toggleControls: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(LocalConfiguration.current.screenHeightDp.dp - paddingValues.calculateTopPadding())
            .padding(paddingValues)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        NowPlayingArtwork(
            bitmap = uiState.artwork,
            toggleControls = toggleControls,
            config = uiState.preferences.artworkDisplayConfig,
            isPaused = uiState.isPaused,
            playbackPosition = uiState.playbackPosition,
            skipPrevious = skipPrevious,
            skipNext = skipNext,
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            NowPlayingMusicInfo(uiState.track)
            AnimatedVisibility(
                visible = showControls,
                enter = slideInVertically { it * 2 },
                exit = slideOutVertically { it * 2 }
            ) {
                Column {
                    NowPlayingProgressBar(
                        progress = uiState.playbackPosition,
                        duration = uiState.track?.duration ?: 0,
                        onSliderMoved = { seek(it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    NowPlayingMusicControls(pauseResume, skipPrevious, skipNext)
                    Spacer(modifier = Modifier.height(16.dp))
                    NowPlayingVolumeSlider(
                        volume = uiState.volume,
                        increaseVolume = increaseVolume,
                        decreaseVolume = decreaseVolume,
                        setVolume = setVolume
                    )
                }
            }
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

@Preview
@Composable
private fun NowPlayingLandscapePreview() {
    NowPlayingLandscapeContent(
        paddingValues = PaddingValues(),
        uiState = AltifyUIState(
            playerContext = AltPlayerContext.example,
            track = AltTrack.example,
        ),
        pauseResume = { /*TODO*/ },
        skipPrevious = { /*TODO*/ },
        skipNext = { /*TODO*/ },
        play = { },
        queue = { },
        seek = { },
        increaseVolume = { /*TODO*/ },
        decreaseVolume = { /*TODO*/ },
        setVolume = {},
        showControls = true
    ) {

    }
}