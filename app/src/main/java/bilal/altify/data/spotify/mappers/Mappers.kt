package bilal.altify.data.spotify.mappers

import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.model.AltPlayerContext
import bilal.altify.domain.model.AltPlayerState
import bilal.altify.domain.model.AltTrack
import com.spotify.protocol.types.ImageUri
import com.spotify.protocol.types.ListItem
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

fun PlayerState.toAlt() =
    AltPlayerState(
        track = this.track.toAlt(),
        isPaused = this.isPaused,
        position = this.playbackPosition,
    )

fun PlayerContext.toAlt() =
    AltPlayerContext(
        uri = this.uri,
        title = this.title,
        subtitle = this.subtitle,
        type = this.type,
    )

fun ListItem.toAlt() =
    AltListItem(
        id = this.id,
        uri = this.uri,
        imageUri = this.imageUri.raw,
        title = this.title,
        subtitle = this.subtitle,
        playable = this.playable,
        hasChildren = this.hasChildren,
    )

fun AltListItem.toOriginal() =
    ListItem(
        /* id = */ id,
        /* uri = */ uri,
        /* imageUri = */ ImageUri(imageUri),
        /* title = */ title,
        /* subtitle = */ subtitle,
        /* playable = */ playable,
        /* hasChildren = */ hasChildren
    )