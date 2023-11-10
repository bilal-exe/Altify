package bilal.altify.domain.model

import java.time.Instant

//data class MediaItemsList<T: MediaItem>(
//    val items: List<T> = emptyList(),
//    val total: Int = 0
//)

//sealed interface ExtendedItem : Item {
//
//    val item: Item
//
//    data class Album(
//        override val remoteId: RemoteId,
//        override val name: String,
//        val albumType: AlbumType = AlbumType.Album,
//        val artist: List<Artist> = emptyList(),
//        val genres: List<Genre> = emptyList(),
//        val label: String? = null,
//        val releaseDate: Instant? = null,
//        val tracks: ExtendedItemList<Track> = ExtendedItemList(),
//        val images: List<Image> = emptyList(),
//    ) : ExtendedItem {
//
//        enum class AlbumType {
//            Album, Single, Compilation
//        }
//
//        override val item: Item.Album = Item.Album(remoteId, name)
//    }
//
//    data class Artist(
//        override val remoteId: RemoteId,
//        override val name: String,
//        val genres: Genres = emptyList(),
//        val images: List<Image> = emptyList(),
//    ) : ExtendedItem {
//
//        override val item: Item.Artist = Item.Artist(remoteId, name)
//
//    }
//
//    data class Playlist(
//        override val remoteId: RemoteId,
//        override val name: String,
//        val images: List<Image> = emptyList(),
//        val tracks: ExtendedItemList<Track> = ExtendedItemList(),
//    ) : ExtendedItem {
//
//        override val item: Item.Playlist = Item.Playlist(remoteId, name)
//
//    }
//
//    data class Track(
//        override val remoteId: RemoteId,
//        override val name: String,
//        val artist: Artist,
//        val artists: List<Artist>,
//        val album: Album,
//        val duration: Long,
//        val imageUri: String?,
//    ) : ExtendedItem {
//
//        override val item: Item = Item.Track(
//            remoteId = remoteId,
//            name = name,
//            artist = artist.item,
//            artists = artists.map { it.item },
//            album = album.item,
//            duration = duration,
//            imageUri = imageUri,
//        )
//
//    }
//
//}
//
//data class ExtendedItemList<T : ExtendedItem>(
//    val totalItems: Int = 0,
//    val items: List<T> = emptyList()
//)
