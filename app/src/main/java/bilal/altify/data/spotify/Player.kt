package bilal.altify.data.spotify

import android.util.Log
import com.spotify.android.appremote.api.PlayerApi
import com.spotify.protocol.types.PlayerContext
import com.spotify.protocol.types.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class Player(
    private val playerApi: PlayerApi,
) {

    private val scope = CoroutineScope(IO)

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack = _currentTrack.asStateFlow()

    private val _isPaused = MutableStateFlow<Boolean>(true)
    val isPaused = _isPaused.asStateFlow()

    private val _playbackPosition = MutableStateFlow<Long>(0)
    val playbackPosition = _playbackPosition.asStateFlow()

    val playerContext: Flow<PlayerContext?> = callbackFlow<PlayerContext?> {
        val subscription = playerApi.subscribeToPlayerContext().setEventCallback {
            trySend(it)
            Log.d("Spotify", "player context")
        }.setErrorCallback {
            throw Exception("Error callback")
        }
        awaitClose { subscription.cancel() }
    }
        .flowOn(IO)
        .stateIn(CoroutineScope(IO), SharingStarted.WhileSubscribed(5000), null)

    init {
        CoroutineScope(IO).launch {
            playerApi.subscribeToPlayerState().setEventCallback {
                Log.d("Spotify", "player state received")
                _currentTrack.value = it.track
                _isPaused.value = it.isPaused
                _playbackPosition.value = it.playbackPosition
            }.setErrorCallback {
                throw Exception("Error callback")
            }
        }
    }

    suspend fun pauseResume() {
        scope.launch {
            when (isPaused.value) {
                true -> playerApi.resume()
                false -> playerApi.pause()
            }
        }
    }

    suspend fun play(uri: String) {
        scope.launch { playerApi.play(uri) }
    }

    suspend fun addToQueue(uri: String) {
        scope.launch { playerApi.queue(uri) }
    }

    suspend fun seek(position: Long) {
        scope.launch { playerApi.seekTo(position) }
    }

    suspend fun skipNext() {
        scope.launch { playerApi.skipNext() }
    }

    suspend fun skipPrevious() {
        scope.launch { playerApi.skipPrevious() }
    }

    suspend fun skipToTrack(uri: String, index: Int) {
        scope.launch { playerApi.skipToIndex(uri, index) }
    }

}