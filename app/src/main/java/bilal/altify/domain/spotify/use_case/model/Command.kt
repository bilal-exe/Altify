package bilal.altify.domain.spotify.use_case.model

import bilal.altify.domain.model.ImageRemoteId
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.model.ListItem
import bilal.altify.domain.model.ListItems

sealed interface Command

sealed interface PlaybackCommand : Command {

    data class PauseResume(val isPaused: Boolean) : PlaybackCommand

    object SkipPrevious : PlaybackCommand

    object SkipNext : PlaybackCommand

    data class Play(val remoteId: RemoteId) : PlaybackCommand

    data class Seek(val position: Long) : PlaybackCommand

    data class SeekRelative(val position: Long) : PlaybackCommand

    data class AddToQueue(val remoteId: RemoteId) : PlaybackCommand

    data class SkipToTrack(val trackId: RemoteId, val index: Int) : PlaybackCommand

    object ToggleShuffle : PlaybackCommand

    object ToggleRepeat : PlaybackCommand

}

sealed interface ContentCommand : Command {

    object GetRecommended : ContentCommand

    data class GetChildrenOfItem(val item: ListItem) : ContentCommand

    data class LoadMoreChildrenOfItem(val listItems: ListItems) : ContentCommand

    object GetPrevious : ContentCommand

    data class Play(val item: ListItem) : ContentCommand

}

sealed interface VolumeCommand : Command {

    object IncreaseVolume : VolumeCommand

    object DecreaseVolume : VolumeCommand

    data class SetVolume(val volume: Float) : VolumeCommand

}

sealed interface ImagesCommand : Command {

    data class GetArtwork(val imageRemoteId: ImageRemoteId) : ImagesCommand

    data class GetThumbnail(val imageRemoteId: ImageRemoteId) : ImagesCommand

    object ClearThumbnails : ImagesCommand

}

sealed interface UserCommand : Command {

    data class UpdateCurrentTrackState(val remoteId: RemoteId) : UserCommand

    data class UpdateBrowserLibraryState(val remoteIds: List<RemoteId>) : UserCommand

    data class ToggleLibraryStatus(val remoteId: RemoteId, val added: Boolean) : UserCommand

}