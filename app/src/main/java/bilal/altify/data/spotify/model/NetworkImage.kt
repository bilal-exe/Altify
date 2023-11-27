package bilal.altify.data.spotify.model

import bilal.altify.data.spotify.model.util.spotifyUrlToId
import bilal.altify.domain.model.Image
import bilal.altify.domain.model.ImageRemoteId
import kotlinx.serialization.Serializable

@Serializable
data class NetworkImage(
    val url: String,
    val height: Int,
    val width: Int
) {

    fun toImage() = Image(
        remoteId = getRemoteId(),
        height = height,
        width = width
    )

    fun getRemoteId() = ImageRemoteId(url.spotifyUrlToId())

}

