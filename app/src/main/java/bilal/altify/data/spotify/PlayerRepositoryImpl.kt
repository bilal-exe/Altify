package bilal.altify.data.spotify

import bilal.altify.data.spotify.mappers.toAlt
import bilal.altify.domain.model.AltPlayerState
import bilal.altify.domain.model.AltPlayerState.Companion.INTERPOLATION_FREQUENCY_MS
import bilal.altify.domain.repository.PlayerRepository
import com.spotify.android.appremote.api.PlayerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerRepositoryImpl(
    private val playerApi: PlayerApi,
) : PlayerRepository {

    private fun playerState() = callbackFlow {

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
            .setErrorCallback {
                throw Exception("Error callback")
            }

        awaitClose { subscription.cancel() }

    }.flowOn(IO)

    private fun playerContext() = callbackFlow {

        val subscription = playerApi.subscribeToPlayerContext()
            .setEventCallback {
                trySend(it)
            }
            .setErrorCallback {
                throw Exception("Error callback")
            }

        awaitClose { subscription.cancel() }

    }.flowOn(IO)

    override fun getPlayerState() =
        combine(
            playerState(),
            playerContext()
        ) { ps, pc ->
            ps.copy(context = pc.toAlt())
        }
            .stateIn(CoroutineScope(IO), SharingStarted.WhileSubscribed(), AltPlayerState())

    override fun pauseResume(isPaused: Boolean) {
        when (isPaused) {
            true -> playerApi.resume()
            false -> playerApi.pause()
        }
    }

    override fun play(uri: String) {
        playerApi.play(uri)
    }

    override fun addToQueue(uri: String) {
        playerApi.queue(uri)
    }

    override fun seek(position: Long) {
        playerApi.seekTo(position)
    }

    override fun skipNext() {
        playerApi.skipNext()
    }

    override fun skipPrevious() {
        playerApi.skipPrevious()
    }

    override fun skipToTrack(uri: String, index: Int) {
        playerApi.skipToIndex(uri, index)
    }

}