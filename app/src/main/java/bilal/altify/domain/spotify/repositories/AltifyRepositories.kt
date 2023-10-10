package bilal.altify.domain.spotify.repositories

data class AltifyRepositories(
    val player: PlayerRepository,
    val content: ContentRepository,
    val images: ImagesRepository,
    val volume: VolumeRepository,
    val user: UserRepository
)