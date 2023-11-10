package bilal.altify.domain.model

import java.time.Instant

data class ListItem(
    val remoteId: RemoteId,
    val imageUri: String?,
    val title: String,
    val subtitle: String,
    val playable: Boolean,
) {
    val id: Int = countId++

    companion object {
        private var countId = 0
    }
}

data class ListItems(
    val total: Int,
    val items: List<ListItem>
)

sealed interface Item {

    val remoteId: RemoteId
    val name: String

    data class Album(
        override val remoteId: RemoteId,
        override val name: String
    ) : Item

    data class Artist(
        override val remoteId: RemoteId,
        override val name: String
    ) : Item

    data class Playlist(
        override val remoteId: RemoteId,
        override val name: String
    ) : Item

    data class Track(
        override val remoteId: RemoteId,
        override val name: String,
        val artist: Artist,
        val artists: List<Artist>,
        val album: Album,
        val duration: Long,
        val imageUri: String?
    ) : Item {
        companion object {
            private val remoteId = RemoteId("", ContentType.Track)
            val example = Track(
                remoteId = remoteId,
                name = "TestingTesting123",
                artist = Artist(remoteId, "Artist"),
                artists = listOf(Artist(remoteId, "Artist1"),Artist(remoteId, "Artist2")),
                album = Album(remoteId, "Album"),
                duration = 0,
                imageUri = ""
            )
        }
    }

}

sealed interface ExtendedItem : Item {

    val item: Item

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
    ) : ExtendedItem {

        enum class AlbumType {
            Album, Single, Compilation
        }

        override val item: Item.Album = Item.Album(remoteId, name)
    }

    data class Artist(
        override val remoteId: RemoteId,
        override val name: String,
        val genres: Genres = emptyList(),
        val images: List<Image> = emptyList(),
    ) : ExtendedItem {

        override val item: Item.Artist = Item.Artist(remoteId, name)

    }

    data class Playlist(
        override val remoteId: RemoteId,
        override val name: String,
        val images: List<Image> = emptyList(),
        val tracks: ExtendedItemList<Track> = ExtendedItemList(),
    ) : ExtendedItem {

        override val item: Item.Playlist = Item.Playlist(remoteId, name)

    }

    data class Track(
        override val remoteId: RemoteId,
        override val name: String,
        val artist: Artist,
        val artists: List<Artist>,
        val album: Album,
        val duration: Long,
        val imageUri: String?,
    ) : ExtendedItem {

        override val item: Item = Item.Track(
            remoteId = remoteId,
            name = name,
            artist = artist.item,
            artists = artists.map { it.item },
            album = album.item,
            duration = duration,
            imageUri = imageUri,
        )

    }

}

data class ExtendedItemList<T : ExtendedItem>(
    val totalItems: Int = 0,
    val items: List<T> = emptyList()
)



