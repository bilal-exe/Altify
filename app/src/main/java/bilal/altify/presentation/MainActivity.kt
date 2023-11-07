package bilal.altify.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import bilal.altify.R
import bilal.altify.domain.spotify.repositories.appremote.SpotifyConnector.Companion.CLIENT_ID
import bilal.altify.domain.spotify.repositories.appremote.SpotifyConnector.Companion.REDIRECT_URI
import bilal.altify.domain.spotify.repositories.appremote.SpotifyConnector.Companion.REQUEST_CODE
import bilal.altify.domain.spotify.use_case.model.VolumeCommand
import bilal.altify.presentation.prefrences.AltPreference
import bilal.altify.presentation.screens.ErrorScreen
import bilal.altify.presentation.screens.ErrorScreenInfo
import bilal.altify.presentation.screens.LoadingScreen
import bilal.altify.presentation.theme.AltifyTheme
import bilal.altify.presentation.util.Icon
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<AltifyViewModel>()

    private var uiState: AltifyUIState by mutableStateOf(AltifyUIState.Connecting)

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
            viewModel.refreshTokenSignalFlow
                .onEach { authorizeSpotifyWebApi() }
                .collect()
        }

        setContent {

            val snackbarHostState = SnackbarHostState()
            val scope = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                scope.launch {
                    viewModel.errors.collectLatest {
                        if (it != null) {
                            snackbarHostState.showSnackbar(
                                message = it,
                                duration = SnackbarDuration.Long
                            )
                        }
                    }
                }
            }

            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) {
                when (val state = uiState) {

                    AltifyUIState.Connecting ->
                        LoadingScreen("Connecting to Spotify...")

                    is AltifyUIState.Disconnected -> {
                        val errorInfo = when (state.error) {
                            is Error.SpotifyConnector -> spotifyConnectorErrorInfo
                            is Error.APIToken -> when (state.error.error) {
                                APITokenError.EXPIRED -> emptyApiTokenErrorInfo
                                APITokenError.EMPTY -> expiredApiTokenErrorInfo
                            }
                        }
                        ErrorScreen(errorInfo)
                    }

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

    // use get instead of direct assignment as You can't request ViewModel before onCreate call
    private val spotifyConnectorErrorInfo get() = ErrorScreenInfo(
        message = "Could not connect to Spotify",
        buttonText = "Retry connection",
        buttonFunc = viewModel::connect,
        buttonFrontIcon = Icon.ImageVectorIcon(Icons.Default.Refresh),
    )

    private val emptyApiTokenErrorInfo get() = ErrorScreenInfo(
        message = "No Spotify API token",
        icon = Icon.DrawableResourceIcon(R.drawable.key_off),
        buttonText = "Refresh key",
        buttonBackIcon = Icon.ImageVectorIcon(Icons.Default.Refresh),
        // todo should i use lifecycle-scope?
        buttonFunc = { CoroutineScope(IO).launch { authorizeSpotifyWebApi() } }
    )

    private val expiredApiTokenErrorInfo get() = ErrorScreenInfo(
        message = "Expired Spotify API token",
        icon = Icon.DrawableResourceIcon(R.drawable.key_off),
        buttonText = "Refresh key",
        buttonBackIcon = Icon.ImageVectorIcon(Icons.Default.Refresh),
        // todo should i use lifecycle-scope?
        buttonFunc = { CoroutineScope(IO).launch { authorizeSpotifyWebApi() } }
    )
}

enum class DarkThemeConfig(override val code: Int, override val title: String) : AltPreference {
    FOLLOW_SYSTEM(0, "Follow System"), LIGHT(1, "Light"), DARK(2, "Dark")
}

fun DarkThemeConfig.shouldUseDarkTheme(): Boolean? = when (this) {
    DarkThemeConfig.FOLLOW_SYSTEM -> null
    DarkThemeConfig.LIGHT -> false
    DarkThemeConfig.DARK -> true
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
    )
}

@Preview(showBackground = true)
@Composable
fun DisconnectedPreview() {
    ErrorScreen(
        message = "Couldn't connect to Spotify",
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