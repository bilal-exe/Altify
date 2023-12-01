package bilal.altify.data.spotify.model

import bilal.altify.domain.model.Queue
import kotlinx.serialization.Serializable

@Serializable
data class NetworkQueue(
    val currently_playing: ExtendedNetworkTrack,
    val queue: List<ExtendedNetworkTrack>
) {

    fun toQueue() =
        Queue(
            currentlyPlaying = currently_playing.toExtendedTrack(),
            queue = queue.map { it.toExtendedTrack() },
        )
}
