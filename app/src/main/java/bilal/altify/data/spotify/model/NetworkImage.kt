package bilal.altify.data.spotify.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkImage(
    val url: String,
    val height: Int,
    val width: Int
)
