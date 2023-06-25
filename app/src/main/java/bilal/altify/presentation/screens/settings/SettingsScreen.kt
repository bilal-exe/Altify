package bilal.altify.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import bilal.altify.presentation.AltifyUIState
import bilal.altify.presentation.AltifyViewModel
import bilal.altify.presentation.screens.nowplaying.NowPlayingBackground

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    uiState: AltifyUIState,
    navToNowPlaying: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background()
    ) {

    }
}