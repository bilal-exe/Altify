package bilal.altify.domain.spotify.use_case

import bilal.altify.domain.spotify.use_case.model.CurrentTrackState
import bilal.altify.domain.spotify.repositories.util.AltifyRepositories
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.milliseconds

class GetCurrentTrackFlowUseCase {

    operator fun invoke(
        repositories: AltifyRepositories
    ) = combine(
        repositories.player.playerState,
        repositories.player.playerContext,
        repositories.volume.volume,
        repositories.images.artworkFlow,
        repositories.user.currentTrackLibraryState
    ) { ps, pc, vol, art, lib ->
        CurrentTrackState(
            track = ps.track,
            isPaused = ps.isPaused,
            playbackPosition = ps.position,
            playerContext = pc,
            volume = vol,
            artwork = art,
            libraryState = lib,
            repeatMode = ps.repeatMode,
            isShuffled = ps.isShuffling
        )
    }
        .interpolatePlaybackPosition()

    companion object {
        val INTERPOLATION_FREQUENCY_MS = 500L.milliseconds
    }

    // interpolates playback position between Spotify callbacks to keep UI updated
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<CurrentTrackState>.interpolatePlaybackPosition() =
        this.flatMapLatest {
            flow {
                emit(it)
                var track = it
                while (!track.isPaused) {
                    delay(INTERPOLATION_FREQUENCY_MS)
                    track = track.copy(
                        playbackPosition = track.playbackPosition + INTERPOLATION_FREQUENCY_MS
                    )
                    emit(track)
                }
            }
        }

}
