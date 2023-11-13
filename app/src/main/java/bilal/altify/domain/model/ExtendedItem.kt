package bilal.altify.domain.model

import java.time.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/*
* Detailed items for full page views
* */
sealed interface ExtendedItem : Item {

    data class Album(
        override val remoteId: RemoteId,
        override val name: String,
        val albumType: AlbumType = AlbumType.Album,
        val artist: List<Artist> = emptyList(),
        val genres: List<Genre> = emptyList(),
        val label: String? = null,
        val releaseDate: Instant? = null,
        val tracks: ExtendedItemList<Track> = ExtendedItemList(),
        val images: List<Image> = emptyList(),
    ) : ExtendedItem, Item.Album {

        enum class AlbumType(val serializedName: String) {
            Album("album"), Single("single"), Compilation("compilation")
        }

    }

    data class Artist(
        override val remoteId: RemoteId,
        override val name: String,
        val genres: Genres = emptyList(),
        val images: List<Image> = emptyList(),
    ) : ExtendedItem, Item.Artist

    data class Playlist(
        override val remoteId: RemoteId,
        override val name: String,
        val images: List<Image> = emptyList(),
        val tracks: ExtendedItemList<Track> = ExtendedItemList(),
    ) : ExtendedItem, Item.Playlist

    data class Track(
        override val remoteId: RemoteId,
        override val name: String,
        override val artist: Artist,
        override val artists: List<Artist>,
        override val album: Album,
        override val duration: Duration,
        override val imageId: ImageRemoteId?,
    ) : ExtendedItem, Item.Track {

        companion object {
            val example = Track(
                remoteId = RemoteId.fake,
                name = "TestingTesting123",
                artist = Artist(RemoteId.fake, "Artist"),
                artists = listOf(
                    Artist(RemoteId.fake, "Artist1"),
                    Artist(RemoteId.fake, "Artist2")
                ),
                album = Album(RemoteId.fake, "Album"),
                duration = 5.minutes,
                imageId = ImageRemoteId("")
            )
        }

    }
}


data class ExtendedItemList<T : ExtendedItem>(
    val totalItems: Int = 0,
    val items: List<T> = emptyList()
)