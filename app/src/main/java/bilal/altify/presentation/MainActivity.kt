package bilal.altify.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import bilal.altify.presentation.prefrences.AltPreference
import bilal.altify.presentation.screens.ErrorScreen
import bilal.altify.presentation.screens.LoadingScreen
import bilal.altify.presentation.screens.preferences.PreferencesViewModel
import bilal.altify.presentation.theme.AltifyTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<AltifyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

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

                    is AltifyConnectionState.Success ->
                        AltifyApp()

                }

            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP ->
                viewModel.executeCommand(VolumeCommand.IncreaseVolume)
            KeyEvent.KEYCODE_VOLUME_DOWN ->
                viewModel.executeCommand(VolumeCommand.DecreaseVolume)
            else -> return false
        }
        return true
    }

}

enum class DarkThemeConfig(override val code: Int, override val title: String) : AltPreference {
    FOLLOW_SYSTEM(0, "Follow System"),
    LIGHT(1, "Light"),
    DARK(2, "Dark")
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AltifyTheme {

    }
}