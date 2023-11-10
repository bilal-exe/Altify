package bilal.altify.data.mappers

import bilal.altify.domain.model.LibraryState
import bilal.altify.domain.model.Item.*
import bilal.altify.domain.model.ListItem
import bilal.altify.domain.model.ListItems
import bilal.altify.domain.model.PlayerContext
import bilal.altify.domain.model.RemoteId
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
        remoteId = this.uri.spotifyUriToRemoteId(),
        name = this.name,
        artist = this.artist.toModel(),
        artists = this.artists.map { it.toModel() },
        album = this.album.toModel(),
        duration = this.duration,
        imageUri = this.imageUri.raw,
    )

fun SpotifyAlbum.toModel() =
    Album(
        remoteId = this.uri.spotifyUriToRemoteId(),
        name = this.name,
    )

fun SpotifyArtist.toModel() =
    Artist(
        name = this.name,
        remoteId = this.uri.spotifyUriToRemoteId(),
    )

fun SpotifyPlayerContext.toModel() =
    PlayerContext(
        remoteId = uri.spotifyUriToRemoteId(),
        title = this.title,
        subtitle = this.subtitle,
        type = this.type,
    )

fun SpotifyListItem.toModel() =
    ListItem(
        remoteId = uri.spotifyUriToRemoteId(),
        imageUri = imageUri.raw,
        title = title,
        subtitle = subtitle,
        playable = playable,
    )

fun SpotifyListItems.toModel() =
    ListItems(
        items = this.items.map { it.toModel() },
        total = this.total
    )

fun SpotifyLibraryState.toModel() =
    LibraryState(
        remoteId = uri.spotifyUriToRemoteId(),
        isAdded = this.isAdded,
        canAdd = this.canAdd
    )

/*
only the `id` and `playable` field is required as the spotify SDK functions `getChildrenOfItem`
and `playContentItem` only use these
*/
fun ListItem.toOriginal(): SpotifyListItem {
    val uri = this.remoteId.toSpotifyUri()
    return com.spotify.protocol.types.ListItem(
        /* id = */ uri,
        /* uri = */ uri,
        /* imageUri = */ ImageUri(imageUri),
        /* title = */ title,
        /* subtitle = */ subtitle,
        /* playable = */ playable,
        /* hasChildren = */ true
    )
}

fun String.spotifyUriToRemoteId() =
    RemoteId(
        remoteId = this.substringAfterLast(':'),
        contentTypeString = this.substringAfter(':').substringBefore(':')
    )


fun RemoteId.toSpotifyUri() =
    "spotify:${this.getContentTypeString()}:$remoteId"

