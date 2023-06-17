package bilal.altify.presentation

import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.PlayerContext
import com.spotify.protocol.types.Track

sealed interface AltifyUIState {

    data class Disconnected(val message: String? = null): AltifyUIState

    object Connecting: AltifyUIState

    data class Connected(
        val track: Track? = null,
        val isPaused: Boolean = true,
        var playbackPosition: Long = 0L,
        val playerContext: PlayerContext? = null,
        val volume: Float = 0f,
        val listItems: Array<ListItem>? = null
    ): AltifyUIState

}