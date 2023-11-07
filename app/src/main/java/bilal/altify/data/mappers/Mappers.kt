package bilal.altify.data.mappers

import bilal.altify.domain.model.LibraryState
import bilal.altify.domain.model.PlayerContext
import bilal.altify.domain.model.*
import com.spotify.protocol.types.ImageUri

typealias SpotifyTrack = com.spotify.protocol.types.Track
typealias SpotifyPlayerContext = com.spotify.protocol.types.PlayerContext
typealias SpotifyListItem = com.spotify.protocol.types.ListItem
typealias SpotifyListItems = com.spotify.protocol.types.ListItems
typealias SpotifyLibraryState = com.spotify.protocol.types.LibraryState
typealias SpotifyAlbum = com.spotify.protocol.types.Album
typealias SpotifyArtist = com.spotify.protocol.types.Artist

fun SpotifyTrack.toModel() =
    MediaItem.Track(
        remoteId = this.uri.uriToSpotifyId(),
        artist = this.artist.toModel(),
        artists = this.artists.map { it.toModel() },
        album = this.album.toModel(),
        duration = this.duration,
        name = this.name,
        imageUri = this.imageUri.raw,
    )

fun SpotifyAlbum.toModel() =
    Album(
        remoteId = this.uri.uriToSpotifyId(),
        name = this.name,
    )

fun SpotifyArtist.toModel() =
    Artist(
        name = this.name,
        remoteId = this.uri.uriToSpotifyId(),
    )

fun SpotifyPlayerContext.toModel() =
    PlayerContext(
        uri = this.uri,
        title = this.title,
        subtitle = this.subtitle,
        type = this.type,
    )

fun SpotifyListItem.toModel() =
    when (uri.substringAfter(':').substringBefore(':')) {
        "album" ->
            Album(
                remoteId = this.uri.uriToSpotifyId(),
                name = this.title,
            )
        "artist" ->
            Artist(
                remoteId = this.uri.uriToSpotifyId(),
                name = this.title,
            )
        "playlist" ->
            Playlist(
                remoteId = this.uri.uriToSpotifyId(),
                name = this.title,

            )
        "track" ->
            Track(
                remoteId = this.uri.uriToSpotifyId(),
                name = this.title,
            )
        "section" -> TODO()
        else -> throw Exception()
    }

fun SpotifyListItems.toModel() =
    MediaItemsList(
        items = this.items.map { it.toModel() },
        total = this.total
    )

fun SpotifyLibraryState.toModel() =
    LibraryState(
        uri = this.uri,
        isAdded = this.isAdded,
        canAdd = this.canAdd
    )

fun MediaItem.toOriginal(): SpotifyListItem {
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

private fun String.uriToSpotifyId() =
    this.substringAfterLast(':')

private fun