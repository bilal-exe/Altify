package bilal.altify.presentation.navigation

enum class AltifyDestination(
    val route: String,
) {
    NowPlaying(route = "now_playing",),
    Settings(route = "browse");
}

val destinations = AltifyDestination.values().asList()