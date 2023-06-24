package bilal.altify.presentation.prefrences

import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.screens.nowplaying.ArtworkDisplayConfig

interface AltPreference {
    val code: Int
}

data class AltPreferencesState(
    val darkTheme: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val artworkDisplayConfig: ArtworkDisplayConfig = ArtworkDisplayConfig.NORMAL,
)