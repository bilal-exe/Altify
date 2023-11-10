package bilal.altify.domain.model.mappers

import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.SimpleItem

fun ExtendedItem.Track.toSimple() =
    SimpleItem.Track(
        remoteId = remoteId,
        name = name,
        artist = artist.toSimple(),
        artists = artists.map { it.toSimple() },
        album = album.toSimple(),
        duration = duration,
        imageId = imageId,
    )

fun ExtendedItem.Artist.toSimple() =
    SimpleItem.Artist(
        remoteId = remoteId,
        name = name
    )

fun ExtendedItem.Album.toSimple() =
    SimpleItem.Album(
        remoteId = remoteId,
        name = name
    )
