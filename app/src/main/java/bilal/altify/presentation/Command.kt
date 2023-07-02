package bilal.altify.presentation

import bilal.altify.domain.model.AltListItem
import com.spotify.protocol.types.ListItem

sealed interface Command

sealed interface PlaybackCommand : Command {

    data class PauseResume(val isPaused: Boolean) : PlaybackCommand

    object SkipPrevious : PlaybackCommand

    object SkipNext : PlaybackCommand

    data class Play(val uri: String) : PlaybackCommand

    data class Seek(val position: Long) : PlaybackCommand

    data class AddToQueue(val uri: String) : PlaybackCommand

    data class SkipToTrack(val uri: String, val index: Int) : PlaybackCommand

}

sealed interface ContentCommand : Command {

    object GetRecommended : ContentCommand

    data class GetChildrenOfItem(val listItem: AltListItem) : ContentCommand

    data class Play(val listItem: ListItem) : ContentCommand

}

sealed interface VolumeCommand : Command {

    object IncreaseVolume : VolumeCommand

    object DecreaseVolume : VolumeCommand

    data class SetVolume(val volume: Float) : VolumeCommand

}