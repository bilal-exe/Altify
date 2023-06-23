package bilal.altify.presentation

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import bilal.altify.presentation.screens.ErrorScreen
import bilal.altify.presentation.screens.LoadingScreen
import bilal.altify.presentation.theme.AltifyTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<AltifyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AltifyTheme {

                val uiState by viewModel.uiState.collectAsState()

                when (uiState) {

                    AltifyUIState.Connecting ->
                        LoadingScreen("Connecting to Spotify...")

                    is AltifyUIState.Disconnected ->
                        ErrorScreen(
                            message = (uiState as AltifyUIState.Disconnected).message
                                ?: "Couldn't connect to Spotify",
                            buttonText = "Tap to retry",
                            buttonFunc = viewModel::connect
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