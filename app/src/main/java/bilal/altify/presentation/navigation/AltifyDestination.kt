package bilal.altify.presentation.navigation

import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink
import bilal.altify.R

enum class AltifyDestination(
    val icon: Int,
    val route: String,
) {
    NOW_PLAYING(
        icon = R.drawable.music,
        route = "now_playing",
    ),
    BROWSE(
        icon = R.drawable.list,
        route = "browse"
    );
}

val destinations = AltifyDestination.values().asList()