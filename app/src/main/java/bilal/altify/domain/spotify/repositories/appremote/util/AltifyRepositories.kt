package bilal.altify.domain.spotify.repositories.appremote.util

import bilal.altify.domain.spotify.repositories.appremote.ContentRepository
import bilal.altify.domain.spotify.repositories.appremote.ImagesRepository
import bilal.altify.domain.spotify.repositories.appremote.PlayerRepository
import bilal.altify.domain.spotify.repositories.appremote.UserRepository
import bilal.altify.domain.spotify.repositories.appremote.VolumeRepository

data class AltifyRepositories(
    val player: PlayerRepository,
    val content: ContentRepository,
    val images: ImagesRepository,
    val volume: VolumeRepository,
    val user: UserRepository
)