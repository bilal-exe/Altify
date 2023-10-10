package bilal.altify.presentation.screens.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilal.altify.domain.prefrences.PreferencesRepository
import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.prefrences.AltPreferencesState
import bilal.altify.presentation.prefrences.ArtworkDisplayConfig
import bilal.altify.presentation.prefrences.BackgroundColourConfig
import bilal.altify.presentation.prefrences.BackgroundStyleConfig
import bilal.altify.presentation.prefrences.FullScreenMusicInfoAlignment
import bilal.altify.presentation.prefrences.MusicInfoAlignmentConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferences: PreferencesRepository,
): ViewModel() {

    val state = preferences.state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AltPreferencesState(),
        )

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

    fun setMusicInfoAlignmentConfig(config: MusicInfoAlignmentConfig) {
        viewModelScope.launch {
            preferences.setMusicInfoAlignmentConfig(config)
        }
    }

    fun setBackgroundColourConfig(config: BackgroundColourConfig) {
        viewModelScope.launch {
            preferences.setBackgroundColourConfig(config)
        }
    }

    fun setFullscreenInfoAlignmentConfig(config: FullScreenMusicInfoAlignment) {
        viewModelScope.launch {
            preferences.setFullscreenInfoAlignmentConfig(config)
        }
    }

}