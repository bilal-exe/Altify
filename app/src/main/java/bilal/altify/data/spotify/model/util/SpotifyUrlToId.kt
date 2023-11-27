package bilal.altify.data.spotify.model.util

fun String.spotifyUrlToId() =
    this.substringAfterLast('/')