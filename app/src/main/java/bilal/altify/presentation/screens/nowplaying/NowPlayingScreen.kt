package bilal.altify.presentation.screens.nowplaying

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bilal.altify.domain.model.AltPlayerContext
import bilal.altify.domain.model.AltTrack
import bilal.altify.presentation.AltifyUIState
import bilal.altify.presentation.AltifyViewModel
import bilal.altify.presentation.Command
import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.PlaybackCommand

@Composable
fun NowPlayingScreen(
    navToSettings: () -> Unit,
    viewModel: AltifyViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    NowPlayingScreen(
        uiState = uiState,
        navToSettings = navToSettings,
        executeCommand = viewModel::executeCommand
    )
}

@Composable
private fun NowPlayingScreen(
    uiState: AltifyUIState,
    navToSettings: () -> Unit,
    executeCommand: (Command) -> Unit
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
                    enter = expandVertically(animationSpec = tween(durationMillis = 1500)),
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 1500))
                ) {
                    if (uiState.playerContext != null) NowPlayingTopBar(
                        player = uiState.playerContext,
                        rightButtonIcon = Icons.Default.Settings,
                        onRightButtonClick = navToSettings
                    )
                }
            },
        ) { paddingValues ->
            val isPortrait = LocalConfiguration.current.orientation != ORIENTATION_LANDSCAPE
            if (isPortrait)
                NowPlayingPortraitContent(
                    paddingValues = paddingValues,
                    uiState = uiState,
                    executeCommand = executeCommand,
                    showControls = showControls,
                    toggleControls = toggleControls,
                    darkTheme = darkTheme
                )
            else
                NowPlayingLandscapeContent(
                    paddingValues = paddingValues,
                    uiState = uiState,
                    executeCommand = executeCommand,
                    showControls = showControls,
                    toggleControls = toggleControls,
                    darkTheme = darkTheme
                )
        }
    }
}

@Composable
private fun NowPlayingPortraitContent(
    paddingValues: PaddingValues,
    uiState: AltifyUIState,
    executeCommand: (Command) -> Unit,
    showControls: Boolean,
    toggleControls: () -> Unit,
    darkTheme: Boolean
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp - paddingValues.calculateTopPadding())
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        NowPlayingArtwork(
            bitmap = uiState.artwork,
            toggleControls = toggleControls,
            config = uiState.preferences.artworkDisplayConfig,
            isPaused = uiState.isPaused,
            executeCommand = executeCommand
        )
        Spacer(modifier = Modifier.weight(1f))
        NowPlayingMusicInfo(
            track = uiState.track,
            config = uiState.preferences.musicInfoAlignmentConfig
        )
        Spacer(modifier = Modifier.weight(1f))
        AnimatedVisibility(
            visible = showControls,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                NowPlayingProgressBar(
                    progress = uiState.playbackPosition,
                    duration = uiState.track?.duration ?: 0,
                    onSliderMoved = { executeCommand(PlaybackCommand.Seek(it)) },
                    darkTheme = darkTheme
                )
                Spacer(modifier = Modifier.height(16.dp))
                NowPlayingMusicControls(
                    executeCommand = executeCommand,
                    isPaused = uiState.isPaused
                )
                Spacer(modifier = Modifier.height(16.dp))
                NowPlayingVolumeSlider(
                    volume = uiState.volume,
                    executeCommand = executeCommand,
                    darkTheme = darkTheme
                )
            }
        }
        if (showControls) Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun NowPlayingLandscapeContent(
    paddingValues: PaddingValues,
    uiState: AltifyUIState,
    executeCommand: (Command) -> Unit,
    showControls: Boolean,
    toggleControls: () -> Unit,
    darkTheme: Boolean,
) {
    Row(
        modifier = Modifier
            .height(LocalConfiguration.current.screenHeightDp.dp - paddingValues.calculateTopPadding())
            .padding(paddingValues)
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NowPlayingArtwork(
            bitmap = uiState.artwork,
            toggleControls = toggleControls,
            config = uiState.preferences.artworkDisplayConfig,
            isPaused = uiState.isPaused,
            executeCommand = executeCommand
        )
        Spacer(modifier = Modifier.width(24.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NowPlayingMusicInfo(
                track = uiState.track,
                config = uiState.preferences.musicInfoAlignmentConfig
            )
            AnimatedVisibility(
                visible = showControls,
                enter = expandIn(),
                exit = shrinkOut()
            ) {
                Column {
                    NowPlayingProgressBar(
                        progress = uiState.playbackPosition,
                        duration = uiState.track?.duration ?: 0,
                        onSliderMoved = { executeCommand(PlaybackCommand.Seek(it)) },
                        darkTheme = darkTheme
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    NowPlayingMusicControls(
                        isPaused = uiState.isPaused,
                        executeCommand = executeCommand
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    NowPlayingVolumeSlider(
                        volume = uiState.volume,
                        darkTheme = darkTheme,
                        executeCommand = executeCommand
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
            playbackPosition = 5000
        ),
        navToSettings = {},
        executeCommand = {}
    )
}

@Preview(name = "NowPlaying", showBackground = true)
@Composable
private fun NowPlayingToggledPreview() {
    NowPlayingPortraitContent(
        paddingValues = PaddingValues(),
        uiState = AltifyUIState(
            playerContext = AltPlayerContext.example,
            track = AltTrack.example,
            playbackPosition = 5000
        ),
        executeCommand = {},
        showControls = false,
        toggleControls = {},
        darkTheme = false
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
            playbackPosition = 5000
        ),
        executeCommand = {},
        showControls = true,
        toggleControls = {},
        darkTheme = false
    )
}