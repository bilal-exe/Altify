package bilal.altify.domain.spotify.use_case

import bilal.altify.domain.spotify.model.AltListItem
import bilal.altify.domain.spotify.model.AltListItems

sealed interface Command

sealed interface PlaybackCommand : Command {

    data class PauseResume(val isPaused: Boolean) : PlaybackCommand

    object SkipPrevious : PlaybackCommand

    object SkipNext : PlaybackCommand

    data class Play(val uri: String) : PlaybackCommand

    data class Seek(val position: Long) : PlaybackCommand

    data class SeekRelative(val position: Long) : PlaybackCommand

    data class AddToQueue(val uri: String) : PlaybackCommand

    data class SkipToTrack(val uri: String, val index: Int) : PlaybackCommand

}

sealed interface ContentCommand : Command {

    object GetRecommended : ContentCommand

    data class GetChildrenOfItem(val listItem: AltListItem) : ContentCommand

    data class LoadMoreChildrenOfItem(val listItems: AltListItems) : ContentCommand

    object GetPrevious : ContentCommand

    data class Play(val listItem: AltListItem) : ContentCommand

}

sealed interface VolumeCommand : Command {

    object IncreaseVolume : VolumeCommand

    object DecreaseVolume : VolumeCommand

    data class SetVolume(val volume: Float) : VolumeCommand

}

sealed interface ImagesCommand : Command {

    data class GetArtwork(val uri: String) : ImagesCommand

    data class GetThumbnail(val uri: String) : ImagesCommand

    object ClearThumbnails : ImagesCommand

}

sealed interface UserCommand : Command {

    data class UpdateCurrentTrackState(val uri: String) : UserCommand

    data class UpdateBrowserLibraryState(val uris: List<String>) : UserCommand

    data class AddToLibrary(val uri: String) : UserCommand

    data class RemoveFromLibrary(val uri: String) : UserCommand

}