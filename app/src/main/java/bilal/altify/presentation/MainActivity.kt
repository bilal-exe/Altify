package bilal.altify.presentation

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import bilal.altify.presentation.prefrences.AltPreference
import bilal.altify.presentation.screens.ErrorScreen
import bilal.altify.presentation.screens.LoadingScreen
import bilal.altify.presentation.screens.settings.SettingsViewModel
import bilal.altify.presentation.theme.AltifyTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<AltifyViewModel>()
    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AltifyTheme {

                val uiState by viewModel.uiState.collectAsState()

                when (uiState.connectionState) {

                    AltifyConnectionState.Connecting ->
                        LoadingScreen("Connecting to Spotify...")

                    is AltifyConnectionState.Disconnected ->
                        ErrorScreen(
                            message = (uiState.connectionState as AltifyConnectionState.Disconnected).message
                                ?: "Couldn't connect to Spotify",
                            buttonText = "Tap to retry",
                            buttonFunc = viewModel::connect
                        )

                    AltifyConnectionState.Success ->
                        AltifyApp(
                            viewModel = viewModel,
                            settingsViewModel = settingsViewModel,
                            uiState = uiState
                        )

                }

            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> viewModel.increaseVolume()
            KeyEvent.KEYCODE_VOLUME_DOWN -> viewModel.decreaseVolume()
            else -> return false
        }
        return true
    }

}

enum class DarkThemeConfig(override val code: Int) : AltPreference {
    FOLLOW_SYSTEM(0), LIGHT(1), DARK(2)
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AltifyTheme {

    }
}