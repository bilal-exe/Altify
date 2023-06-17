package bilal.altify.presentation.navigation

import bilal.altify.R

enum class AltifyDestination(
    val icon: Int,
    val route: String
) {
    NOW_PLAYING(
        icon = R.drawable.music,
        route = "Now Playing"
    ),
    BROWSE(
        icon = R.drawable.list,
        route = "Browse"
    );
}

val destinations = AltifyDestination.values().asList()