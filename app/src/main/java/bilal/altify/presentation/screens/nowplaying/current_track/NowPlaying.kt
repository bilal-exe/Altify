package bilal.altify.presentation.screens.nowplaying.current_track

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
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
import androidx.palette.graphics.Palette
import bilal.altify.domain.model.AltPlayerContext
import bilal.altify.domain.model.AltTrack
import bilal.altify.domain.use_case.Command
import bilal.altify.domain.use_case.PlaybackCommand
import bilal.altify.domain.use_case.VolumeCommand
import bilal.altify.presentation.AltifyUIState
import bilal.altify.presentation.CurrentTrackState
import bilal.altify.presentation.prefrences.FullScreenMusicInfoAlignment

const val NOW_PLAYING_HORIZONTAL_PADDING = 24

@Composable
fun NowPlaying(
    uiState: AltifyUIState,
    navToSettings: () -> Unit,
    executeCommand: (Command) -> Unit,
    palette: Palette? = null,
    darkTheme: Boolean
) {

    var showControls by remember { mutableStateOf(true) }
    val toggleControls = { showControls = !showControls }

    NowPlayingBackground(
        palette = palette,
        darkTheme = darkTheme,
        styleConfig = uiState.preferences.backgroundStyle,
        colourConfig = uiState.preferences.backgroundColour
    ) {
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AnimatedVisibility(
                    visible = showControls,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    if (uiState.trackState.playerContext != null) NowPlayingTopBar(
                        player = uiState.trackState.playerContext,
                        rightButtonIcon = Icons.Default.Settings,
                        onRightButtonClick = navToSettings
                    )
                }
            },
        ) { paddingValues ->
            val isPortrait = LocalConfiguration.current.orientation != Configuration.ORIENTATION_LANDSCAPE
            if (isPortrait)
                NowPlayingPortraitContent(
                    paddingValues = paddingValues,
                    uiState = uiState,
                    executeCommand = executeCommand,
                    showControls = showControls,
                    toggleControls = toggleControls
                )
            else
                NowPlayingLandscapeContent(
                    paddingValues = paddingValues,
                    uiState = uiState,
                    executeCommand = executeCommand,
                    showControls = showControls,
                    toggleControls = toggleControls
                )
        }
    }
}

@Suppress("AnimateAsStateLabel")
@Composable
private fun NowPlayingPortraitContent(
    paddingValues: PaddingValues,
    uiState: AltifyUIState,
    executeCommand: (Command) -> Unit,
    showControls: Boolean,
    toggleControls: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = NOW_PLAYING_HORIZONTAL_PADDING.dp)
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp - paddingValues.calculateTopPadding())
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val bottomSpacingModifier: Modifier =
            when (uiState.preferences.fullScreenInfoAlignment) {
                FullScreenMusicInfoAlignment.MIDDLE -> {
                    val float by animateFloatAsState(targetValue = if (showControls) 0.05f else 1f)
                    Modifier.weight(float)
                }
                FullScreenMusicInfoAlignment.BOTTOM ->
                    Modifier.height(16.dp)
            }

        Spacer(modifier = Modifier.weight(1f))
        NowPlayingArtwork(
            bitmap = uiState.trackState.artwork,
            toggleControls = toggleControls,
            config = uiState.preferences.artworkDisplay,
            isPaused = uiState.trackState.isPaused,
            executeCommand = executeCommand
        )
        Spacer(modifier = Modifier.weight(1f))
        NowPlayingMusicInfo(
            track = uiState.trackState.track,
            config = uiState.preferences.musicInfoAlignment,
            libraryState = uiState.trackState.libraryState,
            executeCommand = executeCommand,
            showControls = showControls
        )
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(
            visible = showControls,
        ) {
            Column {
                NowPlayingProgressBar(
                    progress = uiState.trackState.playbackPosition,
                    duration = uiState.trackState.track?.duration ?: 0,
                    onSliderMoved = { executeCommand(PlaybackCommand.Seek(it)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                NowPlayingMusicControls(
                    executeCommand = executeCommand,
                    isPaused = uiState.trackState.isPaused
                )
                Spacer(modifier = Modifier.height(8.dp))
                NowPlayingVolumeSlider(
                    volume = uiState.trackState.volume,
                    setVolume = { executeCommand(VolumeCommand.SetVolume(it)) }
                )
            }
        }
        Spacer(modifier = bottomSpacingModifier)
    }
}

@Composable
private fun NowPlayingLandscapeContent(
    paddingValues: PaddingValues,
    uiState: AltifyUIState,
    executeCommand: (Command) -> Unit,
    showControls: Boolean,
    toggleControls: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = NOW_PLAYING_HORIZONTAL_PADDING.dp, vertical = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NowPlayingArtwork(
            bitmap = uiState.trackState.artwork,
            toggleControls = toggleControls,
            config = uiState.preferences.artworkDisplay,
            isPaused = uiState.trackState.isPaused,
            executeCommand = executeCommand
        )
        Spacer(modifier = Modifier.width(24.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NowPlayingMusicInfo(
                track = uiState.trackState.track,
                config = uiState.preferences.musicInfoAlignment,
                libraryState = uiState.trackState.libraryState,
                executeCommand = executeCommand,
                showControls = showControls
            )
            AnimatedVisibility(
                visible = showControls,
                enter = expandIn(),
                exit = shrinkOut()
            ) {
                Column {
                    NowPlayingProgressBar(
                        progress = uiState.trackState.playbackPosition,
                        duration = uiState.trackState.track?.duration ?: 0
                    ) { executeCommand(PlaybackCommand.Seek(it)) }
                    Spacer(modifier = Modifier.height(16.dp))
                    NowPlayingMusicControls(
                        isPaused = uiState.trackState.isPaused,
                        executeCommand = executeCommand
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    NowPlayingVolumeSlider(
                        volume = uiState.trackState.volume,
                        setVolume = { executeCommand(VolumeCommand.SetVolume(it)) }
                    )
                }
            }
        }
    }
}

@Preview(name = "NowPlaying")
@Composable
private fun NowPlayingPreview() {
    NowPlaying(
        uiState = AltifyUIState(
            trackState = CurrentTrackState(
                playerContext = AltPlayerContext.example,
                track = AltTrack.example,
                playbackPosition = 5000
            )
        ),
        navToSettings = {},
        executeCommand = {},
        darkTheme = true
    )
}

@Preview(name = "NowPlaying", showBackground = true)
@Composable
private fun NowPlayingToggledPreview() {
    NowPlayingPortraitContent(
        paddingValues = PaddingValues(),
        uiState = AltifyUIState(
            trackState = CurrentTrackState(
                playerContext = AltPlayerContext.example,
                track = AltTrack.example,
                playbackPosition = 5000
            )
        ),
        executeCommand = {},
        showControls = false
    ) {}
}

@Preview
@Composable
private fun NowPlayingLandscapePreview() {
    NowPlayingLandscapeContent(
        paddingValues = PaddingValues(),
        uiState = AltifyUIState(
            trackState = CurrentTrackState(
                playerContext = AltPlayerContext.example,
                track = AltTrack.example,
                playbackPosition = 5000
            )
        ),
        executeCommand = {},
        showControls = true
    ) {}
}