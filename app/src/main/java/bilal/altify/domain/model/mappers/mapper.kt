package bilal.altify.domain.model.mappers

import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.Item

fun ExtendedItem.Track.toSimple() =
    Item.Track(
        remoteId = remoteId,
        name = name,
        artist = artist.toSimple(),
        artists = artists.map { it.toSimple() },
        album = album.toSimple(),
        duration = duration,
        imageUri = imageUri,
    )

fun ExtendedItem.Artist.toSimple() =
    Item.Artist(
        remoteId = remoteId,
        name = name
    )

fun ExtendedItem.Album.toSimple() =
    Item.Album(
        remoteId = remoteId,
        name = name
    )
