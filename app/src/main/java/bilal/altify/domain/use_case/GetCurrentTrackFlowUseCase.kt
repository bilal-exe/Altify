package bilal.altify.domain.use_case

import bilal.altify.domain.controller.AltifySources
import bilal.altify.presentation.CurrentTrackState
import kotlinx.coroutines.flow.combine

class GetCurrentTrackFlowUseCase {

    operator fun invoke(
        repositories: AltifySources
    ) = combine(
        repositories.player.playerStateAndContext,
        repositories.volume.volume,
        repositories.images.artworkFlow,
        repositories.user.currentTrackLibraryState
    ) {
        psc, vol, art, lib ->
        CurrentTrackState(
            track = psc.track,
            isPaused = psc.isPaused,
            playbackPosition = psc.position,
            playerContext = psc.context,
            volume = vol,
            artwork = art,
            libraryState = lib
        )
    }

}