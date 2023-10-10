package bilal.altify.domain.spotify.use_case

import bilal.altify.domain.spotify.repositories.AltifyRepositories
import bilal.altify.domain.spotify.model.AltListItem
import java.util.Stack

class ExecuteCommandUseCase {

    private val browserVisitedHistory: Stack<AltListItem> = Stack()

    companion object {
        const val BROWSER_PER_PAGE = 25
    }

    operator fun invoke(
        command: Command,
        repositories: AltifyRepositories
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
                repositories.content.getChildrenOfItem(command.listItem, BROWSER_PER_PAGE)
            }

            is ContentCommand.LoadMoreChildrenOfItem -> {
                if (command.listItems().size == command.listItems.total) return
                if (browserVisitedHistory.isEmpty()) return
                val listItemsSize = command.listItems().size
                val loadCount = if (listItemsSize + BROWSER_PER_PAGE > command.listItems.total) {
                    command.listItems.total - listItemsSize
                } else BROWSER_PER_PAGE
                repositories.content.loadMoreChildrenOfItem(
                    listItem = browserVisitedHistory.peek(),
                    offset = command.listItems().size,
                    count = loadCount
                )
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
                        repositories.content.getChildrenOfItem(
                            browserVisitedHistory.pop(),
                            BROWSER_PER_PAGE
                        )
                    }
                }
            }

            is ContentCommand.Play -> {
                repositories.content.play(command.listItem)
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
            is ImagesCommand.GetThumbnail -> {
                repositories.images.getThumbnail(command.uri)
            }

            is ImagesCommand.ClearThumbnails -> {
                repositories.images.clearThumbnails()
            }

            is ImagesCommand.GetArtwork -> {
                repositories.images.getArtwork(command.uri)
            }

            // user
            is UserCommand.UpdateCurrentTrackState -> {
                repositories.user.updateCurrentTrackState(command.uri)
            }

            is UserCommand.UpdateBrowserLibraryState -> {
                repositories.user.updateBrowserLibraryState(command.uris)
            }

            is UserCommand.AddToLibrary -> {
                repositories.user.addToLibrary(command.uri)
            }

            is UserCommand.RemoveFromLibrary -> {
                repositories.user.removeFromLibrary(command.uri)
            }
        }

    }

}