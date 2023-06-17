package bilal.altify.presentation

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import bilal.altify.data.SpotifyController
import bilal.altify.presentation.screens.ErrorScreen
import bilal.altify.presentation.screens.LoadingScreen
import bilal.altify.presentation.theme.AltifyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<AltifyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AltifyTheme {

                var uiState: AltifyUIState by mutableStateOf(AltifyUIState.Connecting)

                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.uiState.onEach { uiState = it }.collect()
                    }
                }

                when (uiState) {

                    AltifyUIState.Connecting ->
                        LoadingScreen("Connecting to Spotify")

                    is AltifyUIState.Disconnected ->
                        ErrorScreen(
                            message = (uiState as AltifyUIState.Disconnected).message
                                ?: "Couldn't connect to Spotify"
                        )

                    is AltifyUIState.Connected ->
                        AltifyApp(
                            viewModel = viewModel,
                            uiState = uiState as AltifyUIState.Connected
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AltifyTheme {
    }
}