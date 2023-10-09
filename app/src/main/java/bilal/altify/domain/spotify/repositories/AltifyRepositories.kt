package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.spotify.repositories.ContentRepository
import bilal.altify.domain.spotify.repositories.ImagesRepository
import bilal.altify.domain.spotify.repositories.PlayerRepository
import bilal.altify.domain.spotify.repositories.UserRepository
import bilal.altify.domain.spotify.repositories.VolumeRepository

data class AltifyRepositories(
    val player: PlayerRepository,
    val content: ContentRepository,
    val images: ImagesRepository,
    val volume: VolumeRepository,
    val user: UserRepository
)