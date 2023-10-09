package bilal.altify.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bilal.altify.domain.spotify.use_case.Command
import bilal.altify.presentation.navigation.AltifyDestination
import bilal.altify.presentation.navigation.AltifyNavHost
import bilal.altify.presentation.navigation.destinations

@Composable
fun AltifyApp(
    state: AltifyUIState,
    executeCommand: (Command) -> Unit
) {

    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = currentBackStack?.destination
    val currentScreen = destinations.find { it.route == currentDestination?.route }
        ?: AltifyDestination.NowPlaying
    val snackbarHostState = remember { SnackbarHostState() }

    MaterialTheme(
        colorScheme = when (shouldUseDarkTheme(state)) {
            true -> darkColorScheme()
            false -> lightColorScheme()
        }
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