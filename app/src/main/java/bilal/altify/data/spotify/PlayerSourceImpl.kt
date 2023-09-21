package bilal.altify.data.spotify

import bilal.altify.data.spotify.mappers.toAlt
import bilal.altify.domain.model.AltPlayerStateAndContext
import bilal.altify.domain.sources.PlayerSource
import com.spotify.android.appremote.api.PlayerApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine

class PlayerSourceImpl(
    private val playerApi: PlayerApi,
) : PlayerSource {

    private fun playerState() = callbackFlow {
        val subscription = playerApi.subscribeToPlayerState()
            .setEventCallback {
                trySend(it)
            }
            .setErrorCallback {
                throw Exception("Error callback")
            }
        awaitClose {
            subscription.cancel()
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

    override val playerStateAndContext =
        combine(
            playerState(),
            playerContext()
        ) { playerState, playerContext ->
            AltPlayerStateAndContext(
                track = playerState.track.toAlt(),
                isPaused = playerState.isPaused,
                position = playerState.playbackPosition,
                context = playerContext.toAlt()
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

    override fun seekRelative(position: Long) {
        playerApi.seekToRelativePosition(position)
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