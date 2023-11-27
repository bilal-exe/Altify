package bilal.altify.data.spotify.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCategory(
    val id: String,
    val name: String,
    @SerialName("icons")
    val images: List<NetworkImage>
)

@Serializable
data class NetworkCategories(
    val total: Int,
    val items: List<NetworkCategory>
)