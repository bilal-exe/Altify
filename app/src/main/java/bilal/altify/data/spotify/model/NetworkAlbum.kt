package bilal.altify.data.spotify.model

import bilal.altify.data.spotify.model.util.AlbumTypeSerializer
import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.Genres
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
    val releaseDatePrecision: String,
    val artists: List<NetworkArtist>,
    val tracks: List<NetworkTrack>,
    val genres: Genres,
    val label: String
)