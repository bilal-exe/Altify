package bilal.altify.presentation.prefrences

import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.screens.nowplaying.ArtworkDisplayConfig
import bilal.altify.presentation.screens.nowplaying.BackgroundStyleConfig

interface AltPreference {
    val code: Int
}

data class AltPreferencesState(
    val darkTheme: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val backgroundStyle: BackgroundStyleConfig = BackgroundStyleConfig.SOLID,
    val artworkDisplayConfig: ArtworkDisplayConfig = ArtworkDisplayConfig.NORMAL,
)