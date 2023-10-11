package bilal.altify.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import bilal.altify.domain.spotify.use_case.Command
import bilal.altify.presentation.navigation.AltifyNavHost
import bilal.altify.presentation.theme.AltifyTheme

@Composable
fun AltifyApp(
    state: AltifyUIState,
    executeCommand: (Command) -> Unit
) {

    val navController = rememberNavController()

    AltifyTheme(
        darkTheme = shouldUseDarkTheme(uiState = state),
    ) {
        AltifyNavHost(
            navController = navController,
            executeCommand = executeCommand
        )
    }

}

@Composable
private fun shouldUseDarkTheme(
    uiState: AltifyUIState,
): Boolean = when (uiState) {
    is AltifyUIState.Success -> when (uiState.preferences.darkTheme) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
    else -> isSystemInDarkTheme()
}