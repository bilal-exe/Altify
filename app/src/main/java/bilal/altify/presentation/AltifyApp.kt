package bilal.altify.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bilal.altify.presentation.navigation.AltifyDestination
import bilal.altify.presentation.navigation.AltifyNavHost
import bilal.altify.presentation.navigation.destinations

@Composable
fun AltifyApp(viewModel: AltifyViewModel) {

    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = currentBackStack?.destination
    val currentScreen = destinations.find { it.route == currentDestination?.route }
        ?: AltifyDestination.NowPlaying
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        contentColor = MaterialTheme.colorScheme.onBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        AltifyNavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            viewModel = viewModel
        )
    }

}