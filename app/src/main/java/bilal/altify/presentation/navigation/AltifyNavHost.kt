package bilal.altify.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import bilal.altify.presentation.AltifyUIState
import bilal.altify.presentation.AltifyViewModel
import bilal.altify.presentation.screens.nowplaying.NowPlayingScreen
import bilal.altify.presentation.screens.preferences.PreferencesScreen
import bilal.altify.presentation.screens.preferences.PreferencesViewModel

@Composable
fun AltifyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = AltifyDestination.NowPlaying.route,
    viewModel: AltifyViewModel,
    preferencesViewModel: PreferencesViewModel,
    uiState: AltifyUIState
) {
    val navToNowPlaying = { navController.navigateSingleTopTo(AltifyDestination.NowPlaying.route) }
    val navToSettings = { navController.navigateSingleTopTo(AltifyDestination.Settings.route) }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = AltifyDestination.NowPlaying.route,
            deepLinks = listOf(navDeepLink { uriPattern = "altify://now_playing" })
        ) { NowPlayingScreen(viewModel, uiState, navToSettings) }
        composable(
            route = AltifyDestination.Settings.route
        ) { PreferencesScreen(preferencesViewModel, uiState, navToNowPlaying) }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) { saveState = true }
    }