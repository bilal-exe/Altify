package bilal.altify.presentation.prefrences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.screens.nowplaying.ArtworkDisplayConfig
import bilal.altify.presentation.screens.nowplaying.BackgroundStyleConfig
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AltifyPreferencesDataSource(
    private val altPreferences: DataStore<Preferences>
) {

    private val darkThemeConfigKey = intPreferencesKey("DARK_THEME")
    private val backgroundStyleConfigKey = intPreferencesKey("BACKGROUND_STYLE")
    private val artworkDisplayConfigKey = intPreferencesKey("ARTWORK_DISPLAY")

    val state = altPreferences.data.map { preferences ->
        AltPreferencesState(
            darkTheme = DarkThemeConfig.values()
                .find { it.code == preferences[darkThemeConfigKey] }!!,
            backgroundStyle = BackgroundStyleConfig.values()
                .find { it.code == preferences[backgroundStyleConfigKey] }!!,
            artworkDisplayConfig = ArtworkDisplayConfig.values()
                .find { it.code == preferences[artworkDisplayConfigKey] }!!
        )
    }

    suspend fun setDarkThemeConfig(config: DarkThemeConfig) {
        altPreferences.edit { it[darkThemeConfigKey] = config.code }
    }

    suspend fun setBackgroundStyleConfig(config: BackgroundStyleConfig) {
        altPreferences.edit { it[backgroundStyleConfigKey] = config.code }
    }

    suspend fun setArtworkDisplayConfig(config: ArtworkDisplayConfig) {
        altPreferences.edit { it[artworkDisplayConfigKey] = config.code }
    }

}