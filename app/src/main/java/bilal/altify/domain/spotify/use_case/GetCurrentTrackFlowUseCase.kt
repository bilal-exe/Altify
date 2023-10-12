package bilal.altify.domain.spotify.use_case

import bilal.altify.domain.spotify.model.CurrentTrackState
import bilal.altify.domain.spotify.repositories.AltifyRepositories
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class GetCurrentTrackFlowUseCase {

    operator fun invoke(
        repositories: AltifyRepositories
    ) = combine(
        repositories.player.playerStateAndContext,
        repositories.volume.volume,
        repositories.images.artworkFlow,
        repositories.user.currentTrackLibraryState
    ) { psc, vol, art, lib ->
        CurrentTrackState(
            track = psc.track,
            isPaused = psc.isPaused,
            playbackPosition = psc.position,
            playerContext = psc.context,
            volume = vol,
            artwork = art,
            libraryState = lib,
            repeatMode = psc.repeatMode,
            isShuffled = psc.isShuffling
        )
    }
        .interpolatePlaybackPosition()

    companion object {
        const val INTERPOLATION_FREQUENCY_MS = 500L
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
