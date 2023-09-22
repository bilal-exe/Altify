package bilal.altify.presentation.screens.nowplaying

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import bilal.altify.domain.use_case.ImagesCommand
import bilal.altify.presentation.AltifyViewModel
import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.prefrences.BackgroundStyleConfig
import bilal.altify.presentation.screens.nowplaying.browse.Browser
import bilal.altify.presentation.screens.nowplaying.current_track.NowPlaying
import kotlinx.coroutines.launch

var titleColor by mutableStateOf(Color.Black)
    private set
var bodyColor by mutableStateOf(Color.DarkGray)
    private set

val nowPlayingItemsPadding = PaddingValues(bottom = 8.dp)

@Composable
fun NowPlayingScreen(
    navToSettings: () -> Unit,
    viewModel: AltifyViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    val palette = uiState.trackState.artwork?.let {
        if (uiState.preferences.backgroundStyle != BackgroundStyleConfig.PLAIN)
            Palette.from(it).generate()
        else null
    }

    val darkTheme = when (uiState.preferences.darkTheme) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }

    if (darkTheme) {
        titleColor = Color.White
        bodyColor = Color.LightGray
    } else {
        titleColor = Color.Black
        bodyColor = Color.DarkGray
    }

    LaunchedEffect(key1 = uiState.trackState.track?.imageUri) {
        uiState.trackState.track?.imageUri?.let {
            viewModel.executeCommand(ImagesCommand.GetArtwork(it))
        }
    }

    val scrollState = rememberScrollState()

    val screenBufferBeforeLoad = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    } * 0.15

    LaunchedEffect(key1 = scrollState.value) {
        if (scrollState.value >= (scrollState.maxValue - screenBufferBeforeLoad))
            TODO()
    }

    Scaffold(
        floatingActionButton = { ScrollToTopButton(scrollState) }
    ) { pv ->
        Column(
            modifier = Modifier
                .padding(pv)
                .verticalScroll(scrollState)
        ) {
            NowPlaying(
                uiState = uiState,
                navToSettings = navToSettings,
                executeCommand = viewModel::executeCommand,
                palette = palette,
                darkTheme = darkTheme
            )
            Browser(
                preferences = uiState.preferences,
                track = uiState.trackState.track,
                listItems = uiState.browserState.listItems,
                thumbnailMap = uiState.browserState.thumbnailMap,
                libraryState = uiState.browserState.libraryState,
                executeCommand = viewModel::executeCommand
            )
        }
    }
}

const val BROWSER_FAB_HEIGHT = 65

@Composable
fun ScrollToTopButton(scrollState: ScrollState) {
    val scope = rememberCoroutineScope()
    if (scrollState.value > 0) {
        FloatingActionButton(
            onClick = { scope.launch { scrollState.animateScrollTo(0) } },
            modifier = Modifier
                .height(BROWSER_FAB_HEIGHT.dp)
                .aspectRatio(1f)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "")
        }
    }
}
