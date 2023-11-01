package bilal.altify.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import bilal.altify.domain.spotify.remote.appremote.SpotifyConnector.Companion.CLIENT_ID
import bilal.altify.domain.spotify.remote.appremote.SpotifyConnector.Companion.REDIRECT_URI
import bilal.altify.domain.spotify.remote.appremote.SpotifyConnector.Companion.REQUEST_CODE
import bilal.altify.domain.spotify.use_case.VolumeCommand
import bilal.altify.presentation.prefrences.AltPreference
import bilal.altify.presentation.screens.ErrorScreen
import bilal.altify.presentation.screens.LoadingScreen
import bilal.altify.presentation.theme.AltifyTheme
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
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
                    .onEach { uiState = it }
                    .collect()
            }
        }

        lifecycleScope.launch {
            viewModel.uiState
                .filter { it is AltifyUIState.Disconnected && it.error is Error.APIToken }
                .onEach { authorizeSpotifyWebApi() }
                .collect()
        }


        setContent {

            when (val state = uiState) {
                AltifyUIState.Connecting ->
                    LoadingScreen("Connecting to Spotify...")
                is AltifyUIState.Disconnected ->
                    ErrorScreen(
                        message = /*state.message ?:*/ "Couldn't connect to Spotify",
                        buttonText = "Tap to retry",
                        buttonFunc = viewModel::connect
                    )
                is AltifyUIState.Success ->
                    AltifyApp(state) { viewModel.executeCommand(it, state.repositories) }
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

    private fun authorizeSpotifyWebApi() {
        AuthorizationClient.clearCookies(this)
        val request = AuthorizationRequest.Builder(
            CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI
        )
            .setScopes(arrayOf("streaming"))
            .setShowDialog(true)
            .build()
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    @Deprecated(message = "Required for spotify auth")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_CODE) return
        val response = AuthorizationClient.getResponse(resultCode, data)
        viewModel.onAuthorizationResponse(response)
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
    LoadingScreen(
        "Connecting to Spotify...",
        darkTheme = true
    )
}

@Preview(showBackground = true)
@Composable
fun DisconnectedPreview() {
    ErrorScreen(message = "Couldn't connect to Spotify",
        buttonText = "Tap to retry",
        buttonFunc = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DisconnectedDarkPreview() {
    ErrorScreen(
        message = "Couldn't connect to Spotify",
        buttonText = "Tap to retry",
        buttonFunc = {},
        darkTheme = true
    )
}