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
import bilal.altify.presentation.screens.BrowseScreen
import bilal.altify.presentation.screens.nowplaying.NowPlayingScreen

@Composable
fun AltifyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = AltifyDestination.NOW_PLAYING.route,
    viewModel: AltifyViewModel,
    uiState: AltifyUIState.Connected
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = AltifyDestination.NOW_PLAYING.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "altify://now_playing" }
            )
        ) { NowPlayingScreen(viewModel, uiState) }
        composable(AltifyDestination.BROWSE.route) { BrowseScreen(viewModel, uiState) }
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