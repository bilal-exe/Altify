package bilal.altify.data.spotify.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkQueue(
    val currently_playing: ExtendedNetworkTrack,
    val queue: List<ExtendedNetworkTrack>
)
