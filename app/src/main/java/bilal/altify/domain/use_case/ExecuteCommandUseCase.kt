package bilal.altify.domain.use_case

import bilal.altify.data.spotify.mappers.toOriginal
import bilal.altify.domain.controller.AltifySources
import bilal.altify.domain.model.AltListItem
import java.util.Stack

class ExecuteCommandUseCase {

    private val browserVisitedHistory: Stack<AltListItem> = Stack()
    
    operator fun invoke(
        command: Command,
        repositories: AltifySources
    ) {

        when (command) {

            // playback
            is PlaybackCommand.PauseResume -> {
                repositories.player.pauseResume(command.isPaused)
            }
            PlaybackCommand.SkipPrevious -> {
                repositories.player.skipPrevious()
            }
            PlaybackCommand.SkipNext -> {
                repositories.player.skipNext()
            }
            is PlaybackCommand.Play -> {
                repositories.player.play(command.uri)
            }
            is PlaybackCommand.Seek -> {
                repositories.player.seek(command.position)
            }

            is PlaybackCommand.SeekRelative -> {
                repositories.player.seekRelative(command.position)
            }

            is PlaybackCommand.AddToQueue -> {
                repositories.player.addToQueue(command.uri)
            }
            is PlaybackCommand.SkipToTrack -> {
                repositories.player.skipToTrack(command.uri, command.index)
            }

            // content
            ContentCommand.GetRecommended -> {
                repositories.content.getRecommended()
            }
            is ContentCommand.GetChildrenOfItem -> {
                browserVisitedHistory.add(command.listItem)
                repositories.content.getChildrenOfItem(command.listItem)
            }
            ContentCommand.GetPrevious -> {
                when (browserVisitedHistory.size) {
                    0 ->
                        repositories.content.getRecommended()
                    1 -> {
                        browserVisitedHistory.pop()
                        repositories.content.getRecommended()
                    }
                    else -> {
                        browserVisitedHistory.pop()
                        repositories.content.getChildrenOfItem(browserVisitedHistory.pop())
                    }
                }
            }
            is ContentCommand.Play -> {
                repositories.content.play(command.listItem.toOriginal())
            }

            //volume
            VolumeCommand.DecreaseVolume -> {
                repositories.volume.decreaseVolume()
            }
            VolumeCommand.IncreaseVolume -> {
                repositories.volume.increaseVolume()
            }
            is VolumeCommand.SetVolume -> {
                repositories.volume.setVolume(command.volume)
            }

            // images
            is ImagesCommand.GetThumbnail ->
                repositories.images.getThumbnail(command.uri)

            is ImagesCommand.ClearThumbnails ->
                repositories.images.clearThumbnails()

            is ImagesCommand.GetArtwork ->
                repositories.images.getArtwork(command.uri)

            // user
            is UserCommand.UpdateCurrentTrackState ->
                repositories.user.updateCurrentTrackState(command.uri)

            is UserCommand.UpdateBrowserLibraryState ->
                repositories.user.updateBrowserLibraryState(command.uris)

            is UserCommand.AddToLibrary ->
                repositories.user.addToLibrary(command.uri)

            is UserCommand.RemoveFromLibrary ->
                repositories.user.removeFromLibrary(command.uri)
        }

    }
    
}