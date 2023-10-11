package bilal.altify.presentation.screens.home

import android.graphics.Bitmap
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.palette.graphics.Palette
import bilal.altify.domain.spotify.model.CurrentTrackState
import bilal.altify.domain.spotify.use_case.Command
import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.prefrences.AltPreferencesState
import bilal.altify.presentation.prefrences.BackgroundColourConfig
import bilal.altify.presentation.prefrences.BackgroundStyleConfig
import bilal.altify.presentation.screens.LoadingScreen
import bilal.altify.presentation.screens.home.browse.Browser
import bilal.altify.presentation.screens.home.now_playing.NowPlaying
import bilal.altify.presentation.screens.home.now_playing.NowPlayingUIState
import bilal.altify.presentation.screens.home.now_playing.NowPlayingViewModel
import bilal.altify.presentation.util.getColor
import bilal.altify.presentation.util.shouldUseDarkTheme
import kotlinx.coroutines.launch

val nowPlayingItemsPadding = PaddingValues(bottom = 8.dp)

@Composable
fun HomeScreen(
    navToSettings: () -> Unit,
    viewModel: NowPlayingViewModel = hiltViewModel(),
    executeCommand: (Command) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    when (val uiState = state) {
        NowPlayingUIState.Loading ->
            LoadingScreen()
        is NowPlayingUIState.Success ->
            HomeScreen(
                navToSettings = navToSettings,
                executeCommand = executeCommand,
                uiState = uiState
            )
    }
}

@Composable
private fun HomeScreen(
    navToSettings: () -> Unit,
    executeCommand: (Command) -> Unit,
    uiState: NowPlayingUIState.Success
) {

    val backgroundColor = getBackgroundColor(
        bitmap = uiState.trackState.artwork,
        backgroundColourConfig = uiState.preferences.backgroundColour,
        backgroundStyleConfig = uiState.preferences.backgroundStyle,
        darkThemeConfig = uiState.preferences.darkTheme
    )
    val lazyListState = rememberLazyListState()

//    var overlay: OverlayType? = null
//    LaunchedEffect(key1 = uiState.trackState.volume) {
//        overlay = OverlayType.Volume(uiState.trackState.volume)
//    }

    Scaffold(
        floatingActionButton = { ScrollToTopButton(lazyListState) }
    ) { pv ->
        LazyColumn(
            modifier = Modifier
                .padding(pv),
            state = lazyListState
        ) {
            item {
                NowPlaying(
                    uiState = uiState,
                    navToSettings = navToSettings,
                    executeCommand = executeCommand,
                    backgroundColor = backgroundColor,
                )
            }
            item {
                Browser(
                    preferences = uiState.preferences,
                    playingTrackUri = uiState.trackState.track?.uri,
                    backgroundColor = backgroundColor,
                    executeCommand = executeCommand
                )
            }
        }
//        Overlay(overlayType = overlay)
    }
}

const val BROWSER_FAB_HEIGHT = 65

@Composable
fun ScrollToTopButton(lazyListState: LazyListState) {
    val scope = rememberCoroutineScope()
    val canScrollForward by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 0 }
    }
    if (canScrollForward) {
        FloatingActionButton(
            onClick = { scope.launch { lazyListState.scrollToItem(0) } },
            modifier = Modifier
                .height(BROWSER_FAB_HEIGHT.dp)
                .aspectRatio(1f)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "")
        }
    }
}

@Composable
private fun getBackgroundColor(
    bitmap: Bitmap?,
    backgroundColourConfig: BackgroundColourConfig,
    backgroundStyleConfig: BackgroundStyleConfig,
    darkThemeConfig: DarkThemeConfig
): Color {

    val palette = remember (bitmap, backgroundStyleConfig) {
        bitmap?.let {
            if (backgroundStyleConfig != BackgroundStyleConfig.PLAIN) {
                Palette.from(it).generate()
            } else null
        }
    }

    val darkTheme = shouldUseDarkTheme(darkThemeConfig = darkThemeConfig)
    return when (backgroundColourConfig) {
        BackgroundColourConfig.VIBRANT ->
            if (darkTheme) palette?.darkVibrantSwatch else palette?.lightVibrantSwatch
        BackgroundColourConfig.MUTED ->
            if (darkTheme) palette?.darkMutedSwatch else palette?.lightMutedSwatch
    }
        ?.getColor() ?: MaterialTheme.colorScheme.background
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navToSettings = { /*TODO*/ },
        executeCommand = {},
        uiState = NowPlayingUIState.Success(
            trackState = CurrentTrackState(),
            preferences = AltPreferencesState()
        )
    )
}