package bilal.altify.data.mappers

import bilal.altify.domain.model.ImageRemoteId
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.model.LibraryState
import bilal.altify.domain.model.ListItem
import bilal.altify.domain.model.ListItems
import bilal.altify.domain.model.PlayerContext
import bilal.altify.domain.model.PlayerState
import bilal.altify.domain.model.RepeatMode
import bilal.altify.domain.model.SimpleItem
import kotlin.time.Duration.Companion.milliseconds

typealias SpotifyTrack = com.spotify.protocol.types.Track
typealias SpotifyPlayerContext = com.spotify.protocol.types.PlayerContext
typealias SpotifyListItem = com.spotify.protocol.types.ListItem
typealias SpotifyListItems = com.spotify.protocol.types.ListItems
typealias SpotifyLibraryState = com.spotify.protocol.types.LibraryState
typealias SpotifyAlbum = com.spotify.protocol.types.Album
typealias SpotifyArtist = com.spotify.protocol.types.Artist
typealias SpotifyImage = com.spotify.protocol.types.ImageUri
typealias SpotifyPlayerState = com.spotify.protocol.types.PlayerState

fun SpotifyTrack.toModel() =
    SimpleItem.Track(
        remoteId = this.uri.spotifyUriToRemoteId(),
        name = this.name,
        artist = this.artist.toModel(),
        artists = this.artists.map { it.toModel() },
        album = this.album.toModel(),
        duration = this.duration.milliseconds,
        imageId = this.imageUri.toImageRemoteId(),
    )

fun SpotifyAlbum.toModel() =
    SimpleItem.Album(
        remoteId = this.uri.spotifyUriToRemoteId(),
        name = this.name,
    )

fun SpotifyArtist.toModel() =
    SimpleItem.Artist(
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
        imageRemoteId = imageUri.toImageRemoteId(),
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

private val repeatModes = mapOf(
    0 to RepeatMode.OFF,
    1 to RepeatMode.CONTEXT,
    3 to RepeatMode.TRACK
)

fun SpotifyPlayerState.toModel() =
    PlayerState(
        track = track.toModel(),
        isPaused = isPaused,
        position = playbackPosition.milliseconds,
        repeatMode = repeatModes[playbackOptions.repeatMode]!!,
        isShuffling = playbackOptions.isShuffling
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
        /* imageUri = */ imageRemoteId.toSpotifyImageUri(),
        /* title = */ title,
        /* subtitle = */ subtitle,
        /* playable = */ playable,
        /* hasChildren = */ true
    )
}

fun String.spotifyUriToRemoteId() =
    RemoteId(
        remoteId = substringAfterLast(':'),
        contentTypeString = substringAfter(':').substringBefore(':')
    )

fun SpotifyImage.toImageRemoteId(): ImageRemoteId? {
    return if (this.raw == null || this.raw!!.isBlank()) null
    else {
        val uri = this.raw!!
        when (uri.substring(0, 8)) {
            "spotify:" -> // the raw string is a uri
                ImageRemoteId(this.raw!!.substringAfterLast(':'))
            "https://" -> // is a web link
                ImageRemoteId(this.raw!!)
            else ->
                throw Exception("Unrecognised uri string from imageUri: $uri")
        }
    }
}

fun ImageRemoteId?.toSpotifyImageUri(): SpotifyImage {
    return if (this == null) SpotifyImage(null)
    else {
        if ("https://" in this.remoteId) SpotifyImage(this.remoteId) // web link. leave in raw form
        else SpotifyImage("spotify:image:${this.remoteId}") // reconstruct uri
    }
}

fun RemoteId.toSpotifyUri(): String =
    "spotify:${this.getContentTypeString()}:$id"

