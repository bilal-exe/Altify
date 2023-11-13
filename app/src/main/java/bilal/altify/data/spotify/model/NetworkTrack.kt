package bilal.altify.data.spotify.model

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class NetworkTrack(
    val id: String,
    val artists: List<NetworkArtist>,
    val duration: Duration,
    val explicit: Boolean,
    val name: String
)
