package bilal.altify.data.spotify.repositories

import android.util.Log
import bilal.altify.data.mappers.toModel
import bilal.altify.data.mappers.toSpotifyUri
import bilal.altify.domain.model.PlayerStateAndContext
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.model.RepeatMode
import bilal.altify.domain.spotify.repositories.appremote.PlayerRepository
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
                Log.d("Error", it.message.toString())
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
                Log.d("Error", it.message.toString())
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

    override fun play(remoteId: RemoteId) {
        playerApi.play(remoteId.toSpotifyUri())
    }

    override fun addToQueue(remoteId: RemoteId) {
        playerApi.queue(remoteId.toSpotifyUri())
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

    override fun skipToTrack(remoteId: RemoteId, index: Int) {
        playerApi.skipToIndex(remoteId.toSpotifyUri(), index)
    }

    override fun toggleRepeat() {
        playerApi.toggleRepeat()
    }

    override fun toggleShuffle() {
        playerApi.toggleShuffle()
    }
}