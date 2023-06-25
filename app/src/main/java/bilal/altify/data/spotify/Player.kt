package bilal.altify.data.spotify

import bilal.altify.data.dataclasses.AltPlayerContext
import bilal.altify.data.dataclasses.AltTrack
import com.spotify.android.appremote.api.PlayerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class Player(
    private val playerApi: PlayerApi,
) {

    data class AltPlayerState(
        val track: AltTrack? = null,
        val isPaused: Boolean = true,
        val position: Long = 0L,
        val context: AltPlayerContext? = null
    )

    private val playerState = callbackFlow {

        val subscription = playerApi.subscribeToPlayerState()
            .setEventCallback { trySend(it) }
            .setErrorCallback { throw Exception("Error callback") }

        awaitClose { subscription.cancel() }

    }
        .flowOn(IO)

    private val playerContext = callbackFlow {

        val subscription = playerApi.subscribeToPlayerContext()
            .setEventCallback { trySend(it) }
            .setErrorCallback { throw Exception("Error callback") }

        awaitClose { subscription.cancel() }

    }.flowOn(IO)

    val altPlayerState = combine(
        playerState, playerContext
    ) { ps, pc ->
        AltPlayerState(
            track = ps.track.toAlt(),
            isPaused = ps.isPaused,
            position = ps.playbackPosition,
            context = pc.toAlt()
        )
    }.stateIn(CoroutineScope(IO), SharingStarted.WhileSubscribed(), AltPlayerState())

    fun pauseResume(isPaused: Boolean) {
        when (isPaused) {
            true -> playerApi.resume()
            false -> playerApi.pause()
        }
    }

    fun play(uri: String) {
        playerApi.play(uri)
    }

    fun addToQueue(uri: String) {
        playerApi.queue(uri)
    }

    fun seek(position: Long) {
        playerApi.seekTo(position)
    }

    fun skipNext() {
        playerApi.skipNext()
    }

    fun skipPrevious() {
        playerApi.skipPrevious()
    }

    fun skipToTrack(uri: String, index: Int) {
        playerApi.skipToIndex(uri, index)
    }

}