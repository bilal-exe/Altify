package bilal.altify.presentation.screens.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.prefrences.AltifyPreferencesDataSource
import bilal.altify.presentation.screens.nowplaying.ArtworkDisplayConfig
import bilal.altify.presentation.screens.nowplaying.BackgroundStyleConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferences: AltifyPreferencesDataSource
): ViewModel() {

    val state = preferences.state

    fun setDarkThemeConfig(config: DarkThemeConfig) {
        viewModelScope.launch {
            preferences.setDarkThemeConfig(config)
        }
    }

    fun setBackgroundStyleConfig(config: BackgroundStyleConfig) {
        viewModelScope.launch {
            preferences.setBackgroundStyleConfig(config)
        }
    }

    fun setArtworkDisplayConfig(config: ArtworkDisplayConfig) {
        viewModelScope.launch {
            preferences.setArtworkDisplayConfig(config)
        }
    }

}