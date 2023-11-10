package bilal.altify.data.mappers

import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.Item

fun ExtendedItem.toSpotifyUri(): String {
    val type = when (this) {
        is ExtendedItem.Album -> "album"
        is ExtendedItem.Artist -> "artist"
        is ExtendedItem.Playlist -> "playlist"
        is ExtendedItem.Track -> "track"
    }
    return "spotify:$type:${this.remoteId}"
}

fun Item.toSpotifyUri(): String {
    val type = when (this) {
        is Item.Album -> "album"
        is Item.Artist -> "artist"
        is Item.Playlist -> "playlist"
        is Item.Track -> "track"
    }
    return "spotify:$type:${this.remoteId}"
}