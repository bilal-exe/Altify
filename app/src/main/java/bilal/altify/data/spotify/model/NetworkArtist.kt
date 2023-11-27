package bilal.altify.data.spotify.model

import bilal.altify.domain.model.ContentType
import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.model.SimpleItem
import kotlinx.serialization.Serializable

@Serializable
data class SimpleNetworkArtist(
    val id: String,
    val name: String,
) {

    fun toSimpleArtist() =
        SimpleItem.Artist(
            id = id,
            name = name
        )

}

@Serializable
data class ExtendedNetworkArtist(
    val id: String,
    val name: String,
    val genres: List<String>,
    val images: List<NetworkImage>,
) {

    fun toExtendedArtist() =
        ExtendedItem.Artist(
            remoteId = RemoteId(id, ContentType.Artist),
            name = name,
            genres = genres,
            images = images.map { it.toImage() }
        )

}