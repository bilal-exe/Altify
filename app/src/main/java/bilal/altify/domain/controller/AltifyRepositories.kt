package bilal.altify.domain.controller

import bilal.altify.domain.repository.ContentRepository
import bilal.altify.domain.repository.ImagesRepository
import bilal.altify.domain.repository.PlayerRepository
import bilal.altify.domain.repository.UserRepository
import bilal.altify.domain.repository.VolumeRepository

class AltifyRepositories(
    val player: PlayerRepository,
    val content: ContentRepository,
    val images: ImagesRepository,
    val volume: VolumeRepository,
    val user: UserRepository
)