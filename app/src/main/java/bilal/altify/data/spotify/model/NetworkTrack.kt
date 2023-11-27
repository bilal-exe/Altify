package bilal.altify.data.spotify.model

import bilal.altify.domain.model.ContentType
import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.Item
import bilal.altify.domain.model.ItemList
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.model.SimpleItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class SimpleNetworkTrack(
    val id: String,
    val artists: List<SimpleNetworkArtist>,
    val duration: Duration,
    val explicit: Boolean,
    val name: String
) {

    fun toTrack(album: SimpleItem.Album) =
        SimpleItem.Track(
            remoteId = RemoteId(id, ContentType.Track),
            name = name,
            artists = artists.map { it.toSimpleArtist() },
            artist = artists[0].toSimpleArtist(),
            duration = duration,
            imageId = null,
            album = album
        )

}

@Serializable
data class ExtendedNetworkTrack(
    val id: String,
    val artists: List<ExtendedNetworkArtist>,
    val album: NetworkAlbum,
    val duration: Duration,
    val explicit: Boolean,
    val name: String,
) {

    fun toExtendedTrack() =
        ExtendedItem.Track(
            remoteId = RemoteId(id, ContentType.Track),
            name = name,
            artists = artists.map { it.toExtendedArtist() },
            artist = artists[0].toExtendedArtist(),
            album = album.toExtendedAlbum(),
            duration = duration,
            imageId = album.images.first().getRemoteId()
        )
}

@Serializable
data class NetworkTracks(
    val total: Int,
    @SerialName("items")
    val tracks: List<SimpleNetworkTrack>
) {

    fun toItemList(album: SimpleItem.Album) =
        ItemList<Item.Track>(
            items = tracks.map { it.toTrack(album) },
            totalItems = total
        )

}