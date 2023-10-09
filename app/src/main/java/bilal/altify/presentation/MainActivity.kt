package bilal.altify.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import bilal.altify.domain.spotify.use_case.VolumeCommand
import bilal.altify.presentation.prefrences.AltPreference
import bilal.altify.presentation.screens.ErrorScreen
import bilal.altify.presentation.screens.LoadingScreen
import bilal.altify.presentation.theme.AltifyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<AltifyViewModel>()

    private var uiState: AltifyUIState by mutableStateOf(AltifyUIState.Connecting)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect { }
            }
        }

        setContent {
            AltifyTheme {

                when (val state = uiState) {
                    AltifyUIState.Connecting ->
                        LoadingScreen("Connecting to Spotify...")
                    is AltifyUIState.Disconnected ->
                        ErrorScreen(
                            message = state.message ?: "Couldn't connect to Spotify",
                            buttonText = "Tap to retry",
                            buttonFunc = viewModel::connect
                        )
                    is AltifyUIState.Success ->
                        AltifyApp(state) { viewModel.executeCommand(it, state.repositories) }
                }

            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val state = when (uiState) {
            is AltifyUIState.Success -> uiState as AltifyUIState.Success
            else -> return false
        }
        when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP ->
                viewModel.executeCommand(VolumeCommand.IncreaseVolume, state.repositories)
            KeyEvent.KEYCODE_VOLUME_DOWN ->
                viewModel.executeCommand(VolumeCommand.DecreaseVolume, state.repositories)
            else -> return false
        }
        return true
    }
}

enum class DarkThemeConfig(override val code: Int, override val title: String) : AltPreference {
    FOLLOW_SYSTEM(0, "Follow System"), LIGHT(1, "Light"), DARK(2, "Dark")
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AltifyTheme {}
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    LoadingScreen("Connecting to Spotify...")
}

@Preview(showBackground = true)
@Composable
fun LoadingDarkPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        LoadingScreen("Connecting to Spotify...")
    }
}

@Preview(showBackground = true)
@Composable
fun DisconnectedPreview() {
    ErrorScreen(message = "Couldn't connect to Spotify",
        buttonText = "Tap to retry",
        buttonFunc = {})
}