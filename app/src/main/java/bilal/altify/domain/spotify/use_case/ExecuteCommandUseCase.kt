package bilal.altify.domain.spotify.use_case

import bilal.altify.domain.model.ContentType
import bilal.altify.domain.model.ListItem
import bilal.altify.domain.spotify.repositories.util.AltifyRepositories
import bilal.altify.domain.spotify.use_case.model.Command
import bilal.altify.domain.spotify.use_case.model.ContentCommand
import bilal.altify.domain.spotify.use_case.model.ImagesCommand
import bilal.altify.domain.spotify.use_case.model.PlaybackCommand
import bilal.altify.domain.spotify.use_case.model.UserCommand
import bilal.altify.domain.spotify.use_case.model.VolumeCommand
import java.util.Stack

class ExecuteCommandUseCase {

    private val browserVisitedHistory: Stack<ListItem> = Stack()

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
                repositories.player.play(command.remoteId)
            }

            is PlaybackCommand.Seek -> {
                repositories.player.seek(command.position)
            }

            is PlaybackCommand.SeekRelative -> {
                repositories.player.seekRelative(command.position)
            }

            is PlaybackCommand.AddToQueue -> {
                repositories.player.addToQueue(command.remoteId)
            }

            is PlaybackCommand.SkipToTrack -> {
                if (browserVisitedHistory.isEmpty()) repositories.player.play(command.trackId)
                else repositories.player.skipToTrack(browserVisitedHistory.peek().remoteId, command.index)
            }

            PlaybackCommand.ToggleRepeat -> {
                repositories.player.toggleRepeat()
            }

            PlaybackCommand.ToggleShuffle -> {
                repositories.player.toggleShuffle()
            }

            // content
            ContentCommand.GetRecommended -> {
                repositories.content.getRecommended()
            }

            is ContentCommand.GetChildrenOfItem -> {
                if (command.item.remoteId.contentType == ContentType.Track) return
                browserVisitedHistory.add(command.item)
                repositories.content.getChildrenOfItem(command.item, BROWSER_PER_PAGE)
            }

            is ContentCommand.LoadMoreChildrenOfItem -> {
                if (command.listItems.items.size == command.listItems.total) return
                if (browserVisitedHistory.isEmpty()) return
                val listItemsSize = command.listItems.items.size
                val loadCount = if (listItemsSize + BROWSER_PER_PAGE > command.listItems.total) {
                    command.listItems.total - listItemsSize
                } else BROWSER_PER_PAGE
                repositories.content.loadMoreChildrenOfItem(
                    item = browserVisitedHistory.peek(),
                    offset = command.listItems.items.size,
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
                repositories.content.play(command.item)
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
                repositories.images.getThumbnail(command.imageRemoteId)
            }

            is ImagesCommand.ClearThumbnails -> {
                repositories.images.clearThumbnails()
            }

            is ImagesCommand.GetArtwork -> {
                repositories.images.getArtwork(command.imageRemoteId)
            }

            // user
            is UserCommand.UpdateCurrentTrackState -> {
                repositories.user.updateCurrentTrackState(command.remoteId)
            }

            is UserCommand.UpdateBrowserLibraryState -> {
                repositories.user.updateBrowserLibraryState(command.remoteIds)
            }

            is UserCommand.ToggleLibraryStatus -> {
                repositories.user.toggleLibraryStatus(command.remoteId, command.added)
            }
        }

    }

}