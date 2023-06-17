package bilal.altify.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bilal.altify.presentation.navigation.*

@Composable
fun AltifyApp(
    viewModel: AltifyViewModel,
    uiState: AltifyUIState
) {

    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = currentBackStack?.destination
    val currentScreen = destinations.find { it.route == currentDestination?.route }
        ?: AltifyDestination.NOW_PLAYING

    Scaffold(
        bottomBar = {
            AltifyBottomNav(
                destinations = destinations,
                onNavigateToDestination = { navController.navigateSingleTopTo(it.route) },
                currentDestination = currentScreen
            )
        }
    ) { padding ->
        AltifyNavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            viewModel = viewModel
        )
    }

}