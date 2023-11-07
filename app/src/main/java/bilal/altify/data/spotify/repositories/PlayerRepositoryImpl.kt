package bilal.altify.data.spotify.repositories

import bilal.altify.data.spotify.mappers.toModel
import bilal.altify.domain.spotify.model.PlayerStateAndContext
import bilal.altify.domain.spotify.model.RepeatMode
import bilal.altify.domain.spotify.repositories.PlayerRepository
import com.spotify.android.appremote.api.PlayerApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine

class PlayerRepositoryImpl(
    private val playerApi: PlayerApi,
) : PlayerRepository {

    private fun playerState() = callbackFlow {
        val subscription = playerApi.subscribeToPlayerState()
            .setEventCallback {
                trySend(it)
            }
            .setErrorCallback {
                throw PlayerRepository.PlayerSourceException(it.localizedMessage)
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
                throw PlayerRepository.PlayerSourceException(it.localizedMessage)
            }

        awaitClose { subscription.cancel() }

    }

    private val repeatModes = mapOf(
        0 to RepeatMode.OFF,
        1 to RepeatMode.CONTEXT,
        3 to RepeatMode.TRACK
    )

    override val playerStateAndContext =
        combine(
            playerState(),
            playerContext()
        ) { playerState, playerContext ->
            PlayerStateAndContext(
                track = playerState.track.toModel(),
                isPaused = playerState.isPaused,
                position = playerState.playbackPosition,
                context = playerContext.toModel(),
                repeatMode = repeatModes[playerState.playbackOptions.repeatMode] ?: RepeatMode.OFF,
                isShuffling = playerState.playbackOptions.isShuffling
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

    override fun toggleRepeat() {
        playerApi.toggleRepeat()
    }

    override fun toggleShuffle() {
        playerApi.toggleShuffle()
    }
}