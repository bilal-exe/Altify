package bilal.altify.presentation.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import bilal.altify.presentation.AltifyUIState
import bilal.altify.presentation.DarkThemeConfig

@Composable
private fun shouldUseDarkTheme(uiState: AltifyUIState): Boolean =
    when (uiState) {
        AltifyUIState.Connecting -> isSystemInDarkTheme()
        is AltifyUIState.Disconnected -> isSystemInDarkTheme()
        is AltifyUIState.Connected -> when (uiState.preferences.darkTheme) {
            DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
            DarkThemeConfig.LIGHT -> false
            DarkThemeConfig.DARK -> true
        }
    }