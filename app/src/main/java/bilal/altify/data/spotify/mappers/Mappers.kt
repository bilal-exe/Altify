package bilal.altify.data.spotify.mappers

import bilal.altify.domain.spotify.model.*
import com.spotify.protocol.types.ImageUri

typealias SpotifyTrack = com.spotify.protocol.types.Track
typealias SpotifyPlayerContext = com.spotify.protocol.types.PlayerContext
typealias SpotifyListItem = com.spotify.protocol.types.ListItem
typealias SpotifyListItems = com.spotify.protocol.types.ListItems
typealias SpotifyLibraryState = com.spotify.protocol.types.LibraryState
typealias SpotifyAlbum = com.spotify.protocol.types.Album
typealias SpotifyArtist = com.spotify.protocol.types.Artist

fun SpotifyTrack.toModel() =
    Track(
        artist = this.artist.toModel(),
        artists = this.artists.map { it.toModel() },
        album = this.album.toModel(),
        duration = this.duration,
        name = this.name,
        uri = this.uri,
        imageUri = this.imageUri.raw,
    )

fun SpotifyAlbum.toModel() =
    Album(
        name = this.name,
        uri = this.uri
    )

fun SpotifyArtist.toModel() =
    Artist(
        name = this.name,
        uri = this.uri
    )

fun SpotifyPlayerContext.toModel() =
    PlayerContext(
        uri = this.uri,
        title = this.title,
        subtitle = this.subtitle,
        type = this.type,
    )

private val spotifyUriToType = mapOf(
     "album" to ContentType.Album,
     "artist" to ContentType.Artist,
     "playlist" to ContentType.Playlist,
     "track" to ContentType.Track,
     "section" to ContentType.Section,
)
private val typeToSpotifyUri = spotifyUriToType.entries.associate { it.value to it.key }

fun SpotifyListItem.toModel() =
    ListItem(
        uri = this.uri,
        imageUri = this.imageUri.raw,
        title = this.title,
        subtitle = this.subtitle,
        playable = this.playable,
        hasChildren = this.hasChildren,
        type = spotifyUriToType[uri.substringAfter(':').substringBefore(':')] ?: ContentType.Track
    )

fun ListItem.getSpotifyUri() =
    this.uri.substringAfterLast(':')

fun SpotifyListItems.toModel() =
    ListItems(
        items = this.items.map { it.toModel() },
        total = this.total
    )

fun SpotifyLibraryState.toModel() =
    LibraryState(
        uri = this.uri,
        isAdded = this.isAdded,
        canAdd = this.canAdd
    )

fun ListItem.toOriginal(): SpotifyListItem {
    return com.spotify.protocol.types.ListItem(
        /* id = */ uri,
        /* uri = */ uri,
        /* imageUri = */ ImageUri(imageUri),
        /* title = */ title,
        /* subtitle = */ subtitle,
        /* playable = */ playable,
        /* hasChildren = */ hasChildren
    )
}