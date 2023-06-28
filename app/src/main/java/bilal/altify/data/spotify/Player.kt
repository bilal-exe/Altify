package bilal.altify.data.spotify

import bilal.altify.data.dataclasses.AltPlayerContext
import bilal.altify.data.dataclasses.AltTrack
import bilal.altify.data.spotify.Player.AltPlayerState.Companion.INTERPOLATION_FREQUENCY_MS
import com.spotify.android.appremote.api.PlayerApi
import com.spotify.protocol.types.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class Player(
    private val playerApi: PlayerApi,
) {

    data class AltPlayerState(
        val track: AltTrack? = null,
        val isPaused: Boolean = true,
        val position: Long = 0L,
        val context: AltPlayerContext? = null
    ){
        companion object {
            const val INTERPOLATION_FREQUENCY_MS = 500L
        }
    }

    private val playerState = callbackFlow {

        var job: Job? = null
        val block: suspend (AltPlayerState) -> Unit = {
            if (!it.isPaused) {
                var pos = it.position
                while (true) {
                    delay(INTERPOLATION_FREQUENCY_MS)
                    pos += INTERPOLATION_FREQUENCY_MS
                    trySend(it.copy(position = pos))
                }
            }
        }

        val subscription = playerApi.subscribeToPlayerState()
            .setEventCallback {
                val alt = it.toAlt()
                trySend(alt)
                job?.cancel()
                job = CoroutineScope(IO).launch { block(alt) }
            }
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
        ps.copy(context = pc.toAlt())
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