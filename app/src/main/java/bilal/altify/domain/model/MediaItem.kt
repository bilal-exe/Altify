package bilal.altify.domain.model

import java.time.Instant

//data class MediaItemsList<T: MediaItem>(
//    val items: List<T> = emptyList(),
//    val total: Int = 0
//)

sealed class MediaItem {

    val id: Int = countId++
    abstract val remoteId: String
    abstract val name: String

    companion object {
        private var countId = 0
    }

    data class Album(
        override val remoteId: String,
        override val name: String,
        val albumType: AlbumType = AlbumType.Album,
        val artist: List<Artist> = emptyList(),
        val genres: List<Genre> = emptyList(),
        val label: String? = null,
        val releaseDate: Instant? = null,
        val tracks: MediaItemsList<Track> = MediaItemsList(),
        val images: List<Image> = emptyList(),
    ) : MediaItem() {

        enum class AlbumType {
            Album, Single, Compilation
        }
    }

    data class Artist(
        override val remoteId: String,
        override val name: String,
        val genres: Genres = emptyList(),
        val images: List<Image> = emptyList(),
    ) : MediaItem()

    data class Playlist(
        override val remoteId: String,
        override val name: String,
        val images: List<Image> = emptyList(),
        val tracks: MediaItemsList<Track> = MediaItemsList(),
    ) : MediaItem()

    data class Track(
        override val remoteId: String,
        val artist: Artist,
        val artists: List<Artist>,
        val album: Album,
        val duration: Long,
        override val name: String,
        val imageUri: String?,
    ) : MediaItem()

}