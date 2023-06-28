package bilal.altify.data.spotify

import bilal.altify.data.dataclasses.AltListItem
import bilal.altify.data.dataclasses.AltPlayerContext
import bilal.altify.data.dataclasses.AltTrack
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
    Player.AltPlayerState(
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
        imageUri = this.imageUri,
        title = this.title,
        subtitle = this.subtitle,
        playable = this.playable,
        hasChildren = this.hasChildren,
    )