package bilal.altify.data.spotify.mappers

import bilal.altify.domain.spotify.model.AltLibraryState
import bilal.altify.domain.spotify.model.AltListItem
import bilal.altify.domain.spotify.model.AltListItems
import bilal.altify.domain.spotify.model.AltPlayerContext
import bilal.altify.domain.spotify.model.AltPlayerStateAndContext
import bilal.altify.domain.spotify.model.AltTrack
import bilal.altify.domain.spotify.model.ContentType
import com.spotify.protocol.types.ImageUri
import com.spotify.protocol.types.LibraryState
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.ListItems
import com.spotify.protocol.types.PlayerContext
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track

fun Track?.toAlt() =
    if (this != null) AltTrack(
        artist = this.artist.name,
        album = this.album.name,
        duration = this.duration,
        name = this.name,
        uri = this.uri,
        imageUri = this.imageUri.raw,
    ) else null

fun PlayerContext.toAlt() =
    AltPlayerContext(
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

fun ListItem.toAlt() =
    AltListItem(
        uri = this.uri,
        imageUri = this.imageUri.raw,
        title = this.title,
        subtitle = this.subtitle,
        playable = this.playable,
        hasChildren = this.hasChildren,
        type = spotifyUriToType[uri.substringAfter(':').substringBefore(':')] ?: ContentType.Track
    )

fun ListItems.toAlt() =
    AltListItems(
        items = this.items.map { it.toAlt() },
        total = this.total
    )

fun LibraryState.toAlt() =
    AltLibraryState(
        uri = this.uri,
        isAdded = this.isAdded,
        canAdd = this.canAdd
    )

fun AltListItem.toOriginal() =
    ListItem(
        /* id = */ uri,
        /* uri = */ uri,
        /* imageUri = */ ImageUri(imageUri),
        /* title = */ title,
        /* subtitle = */ subtitle,
        /* playable = */ playable,
        /* hasChildren = */ hasChildren
    )