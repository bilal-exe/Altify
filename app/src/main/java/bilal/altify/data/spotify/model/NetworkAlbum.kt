package bilal.altify.data.spotify.model

import bilal.altify.data.spotify.model.util.AlbumTypeSerializer
import bilal.altify.data.spotify.model.util.getDateWithPrecision
import bilal.altify.domain.model.ContentType
import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.Genres
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.model.SimpleItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAlbum(
    val id: String,
    val name: String,
    @Serializable(AlbumTypeSerializer::class)
    val albumType: ExtendedItem.Album.AlbumType,
    val images: List<NetworkImage>,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("release_date_precision")
    val releaseDatePrecision: String,
    val artists: List<SimpleNetworkArtist>,
    val tracks: NetworkTracks,
    val genres: Genres,
    val label: String
) {

    fun toSimpleAlbum() =
        SimpleItem.Album(
            id, name
        )

    fun toExtendedAlbum() =
        ExtendedItem.Album(
            remoteId = RemoteId(id, ContentType.Album),
            name = name,
            albumType = albumType,
            artist = artists.map { it.toSimpleArtist() },
            genres = genres,
            label = label,
            releaseDate = getDateWithPrecision(date = releaseDate, precision = releaseDatePrecision),
            tracks = tracks.toItemList(this.toSimpleAlbum())
        )

}
