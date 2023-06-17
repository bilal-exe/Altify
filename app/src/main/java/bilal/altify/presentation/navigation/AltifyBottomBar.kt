package bilal.altify.presentation.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun AltifyBottomNav(
    modifier: Modifier = Modifier,
    destinations: List<AltifyDestination>,
    onNavigateToDestination: (AltifyDestination) -> Unit,
    currentDestination: AltifyDestination
) {

    NavigationBar(
        modifier = modifier,
        contentColor = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 0.dp,
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination == destination
            AltifyNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = { NavigationBarItemIcon(destination, selected) },
                modifier = modifier
            )
        }
    }

}

@Composable
fun RowScope.AltifyNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
    )
}

@Composable
fun NavigationBarItemIcon(destination: AltifyDestination, isSelected: Boolean) {
    Icon(
        painter = painterResource(id = destination.icon),
        contentDescription = destination.route,
        tint = if (isSelected) Color.White else Color.Black
    )
}