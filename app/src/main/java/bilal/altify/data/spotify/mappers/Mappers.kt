package bilal.altify.data.spotify.mappers

import bilal.altify.domain.spotify.model.*
import com.spotify.protocol.types.*

fun com.spotify.protocol.types.Track?.toModel() =
    if (this != null) bilal.altify.domain.spotify.model.Track(
        artist = this.artist.name,
        album = this.album.name,
        duration = this.duration,
        name = this.name,
        uri = this.uri,
        imageUri = this.imageUri.raw,
    ) else null

fun com.spotify.protocol.types.PlayerContext.toModel() =
    bilal.altify.domain.spotify.model.PlayerContext(
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

fun com.spotify.protocol.types.ListItem.toModel() =
    bilal.altify.domain.spotify.model.ListItem(
        uri = this.uri,
        imageUri = this.imageUri.raw,
        title = this.title,
        subtitle = this.subtitle,
        playable = this.playable,
        hasChildren = this.hasChildren,
        type = spotifyUriToType[uri.substringAfter(':').substringBefore(':')] ?: ContentType.Track
    )

fun bilal.altify.domain.spotify.model.ListItem.getSpotifyUri() =
    this.uri.substringAfterLast(':')

fun com.spotify.protocol.types.ListItems.toModel() =
    bilal.altify.domain.spotify.model.ListItems(
        items = this.items.map { it.toModel() },
        total = this.total
    )

fun com.spotify.protocol.types.LibraryState.toModel() =
    bilal.altify.domain.spotify.model.LibraryState(
        uri = this.uri,
        isAdded = this.isAdded,
        canAdd = this.canAdd
    )

fun bilal.altify.domain.spotify.model.ListItem.toOriginal(): com.spotify.protocol.types.ListItem {
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