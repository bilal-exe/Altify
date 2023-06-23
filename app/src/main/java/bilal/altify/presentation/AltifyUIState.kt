package bilal.altify.presentation

import bilal.altify.data.dataclasses.AltListItem
import bilal.altify.data.dataclasses.AltPlayerContext
import bilal.altify.data.dataclasses.AltTrack
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.PlayerContext
import com.spotify.protocol.types.Track

sealed interface AltifyUIState {

    data class Disconnected(val message: String? = null): AltifyUIState

    object Connecting: AltifyUIState

    data class Connected(
        val track: AltTrack? = null,
        val isPaused: Boolean = true,
        var playbackPosition: Long = 0L,
        val playerContext: AltPlayerContext? = null,
        val volume: Float = 0f,
        val listItems: Array<AltListItem>? = null
    ): AltifyUIState

}