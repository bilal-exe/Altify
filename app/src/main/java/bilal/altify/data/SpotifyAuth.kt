package bilal.altify.data

import android.content.Context
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import com.spotify.protocol.error.SpotifyAppRemoteException
import com.spotify.protocol.types.PlaybackPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SpotifyController private constructor(
    private val spotifyAppRemote: SpotifyAppRemote
) {

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack = _currentTrack.asStateFlow()

    private val _isPaused = MutableStateFlow<Boolean>(true)
    val isPaused = _isPaused.asStateFlow()

    private val _playbackPosition = MutableStateFlow<Long>(0)
    val playbackPosition = _playbackPosition.asStateFlow()

    val currentPlayer = callbackFlow {

        playerApi.subscribeToPlayerContext().setEventCallback {
            trySend(it)
        }

    }

    private val playerApi = spotifyAppRemote.playerApi

    init {
        CoroutineScope(IO).launch {
            playerApi.subscribeToPlayerState().setEventCallback {
                _currentTrack.value = it.track
                _isPaused.value = it.isPaused
                _playbackPosition.value = it.playbackPosition
            }
        }
    }

    suspend fun pauseResume() {
        when (isPaused.value) {
            true -> playerApi.resume()
            false -> playerApi.pause()
        }
    }

    suspend fun play(uri: String) {
        playerApi.play(uri)
    }

    suspend fun addToQueue(uri: String) {
        playerApi.queue(uri)
    }

    suspend fun seek(position: Long) {
        playerApi.seekTo(position)
    }

    class SpotifyControllerConnector @Inject constructor(
        private val context: Context
    ) {

        private val clientId = "12f6aacd893a4b66b4aa9d6948a45674"
        private val redirectUri = "altify"
        private var controller: SpotifyController? = null

        private val connectionParams: ConnectionParams =
            ConnectionParams
                .Builder(clientId)
                .setRedirectUri(redirectUri)
                .showAuthView(true)
                .build()

        suspend fun connect(): Result<SpotifyController> = suspendCoroutine { continuation ->

            val listener = object : Connector.ConnectionListener {

                override fun onConnected(appRemote: SpotifyAppRemote) =
                    continuation.resume(Result.success(SpotifyController(appRemote)))

                override fun onFailure(throwable: Throwable) =
                    continuation.resume(Result.failure(throwable as SpotifyAppRemoteException))

            }

            SpotifyAppRemote.connect(context, connectionParams, listener)

        }
    }

}