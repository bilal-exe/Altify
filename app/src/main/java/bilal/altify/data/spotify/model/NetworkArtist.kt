package bilal.altify.data.spotify.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkArtist(
    val id: String,
    val name: String,
)
