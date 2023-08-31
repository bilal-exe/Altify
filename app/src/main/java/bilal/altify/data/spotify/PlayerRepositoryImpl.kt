package bilal.altify.data.spotify

import bilal.altify.data.spotify.mappers.toAlt
import bilal.altify.domain.model.AltPlayerStateAndContext
import bilal.altify.domain.repository.PlayerRepository
import com.spotify.android.appremote.api.PlayerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class PlayerRepositoryImpl(
    private val playerApi: PlayerApi,
) : PlayerRepository {

    private fun playerState() = callbackFlow {
        playerApi.subscribeToPlayerState()
            .setEventCallback {
                trySend(it)
            }
            .setErrorCallback {
                throw Exception("Error callback")
            }
    }


    private fun playerContext() = callbackFlow {

        val subscription = playerApi.subscribeToPlayerContext()
            .setEventCallback {
                trySend(it)
            }
            .setErrorCallback {
                throw Exception("Error callback")
            }

        awaitClose { subscription.cancel() }

    }
        .flowOn(IO)

    override fun getPlayerStateAndContext() =
        combine(
            playerState(),
            playerContext()
        ) { ps, pc ->
            AltPlayerStateAndContext(
                track = ps.track.toAlt(),
                isPaused = ps.isPaused,
                position = ps.playbackPosition,
                context = pc.toAlt()
            )
        }

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