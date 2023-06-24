package bilal.altify.presentation.prefrences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.screens.nowplaying.ArtworkDisplayConfig
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AltifyPreferencesDataSource @Inject constructor(
    private val altPreferences: DataStore<Preferences>
) {

    val darkThemeConfigKey = intPreferencesKey("DARK_THEME")
    val artworkDisplayConfigKey = intPreferencesKey("ARTWORK_DISPLAY")

    val state = altPreferences.data.map { preferences ->
        AltPreferencesState(
            darkTheme = DarkThemeConfig.values()
                .find { it.code == preferences[darkThemeConfigKey] }!!,
            artworkDisplayConfig = ArtworkDisplayConfig.values()
                .find { it.code == preferences[artworkDisplayConfigKey] }!!
        )
    }

    suspend fun setDarkThemeConfig(config: DarkThemeConfig) {
        altPreferences.edit { it[darkThemeConfigKey] = config.code }
    }

    suspend fun setArtworkDisplayConfig(config: ArtworkDisplayConfig) {
        altPreferences.edit { it[artworkDisplayConfigKey] = config.code }
    }

}