package bilal.altify.domain.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/*
* Items with minimal information for displaying in browsable lists
* */
sealed interface SimpleItem : Item {

    data class Album(
        override val remoteId: RemoteId,
        override val name: String
    ) : SimpleItem, Item.Album {
        constructor(id: String, name: String) : this(RemoteId(id, ContentType.Album), name)
    }

    data class Artist(
        override val remoteId: RemoteId,
        override val name: String
    ) : SimpleItem, Item.Artist {
        constructor(id: String, name: String) : this(RemoteId(id, ContentType.Artist), name)
    }

    data class Playlist(
        override val remoteId: RemoteId,
        override val name: String
    ) : SimpleItem, Item.Playlist {
        constructor(id: String, name: String) : this(RemoteId(id, ContentType.Playlist), name)
    }

    data class Track(
        override val remoteId: RemoteId,
        override val name: String,
        override val artist: Artist,
        override val artists: List<Artist>,
        override val album: Album,
        override val duration: Duration,
        override val imageId: ImageRemoteId?
    ) : SimpleItem, Item.Track {
        companion object {
            val fake = Track(
                remoteId = RemoteId.fake,
                name = "TestingTesting123",
                artist = Artist(RemoteId.fake, "Artist"),
                artists = listOf(Artist(RemoteId.fake, "Artist1"), Artist(RemoteId.fake, "Artist2")),
                album = Album(RemoteId.fake, "Album"),
                duration = 5.minutes,
                imageId = ImageRemoteId("")
            )
        }
    }
}