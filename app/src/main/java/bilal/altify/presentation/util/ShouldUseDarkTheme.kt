package bilal.altify.presentation.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import bilal.altify.presentation.AltifyConnectionState
import bilal.altify.presentation.AltifyUIState
import bilal.altify.presentation.DarkThemeConfig

@Composable
private fun shouldUseDarkTheme(uiState: AltifyUIState): Boolean =
    when (uiState.connectionState) {
        AltifyConnectionState.Connecting -> isSystemInDarkTheme()
        is AltifyConnectionState.Disconnected -> isSystemInDarkTheme()
        AltifyConnectionState.Success -> when (uiState.preferences.darkTheme) {
            DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
            DarkThemeConfig.LIGHT -> false
            DarkThemeConfig.DARK -> true
        }
    }