package bilal.altify.domain.controller

import bilal.altify.domain.sources.ContentSource
import bilal.altify.domain.sources.ImagesSource
import bilal.altify.domain.sources.PlayerSource
import bilal.altify.domain.sources.UserSource
import bilal.altify.domain.sources.VolumeSource

data class AltifySources(
    val player: PlayerSource,
    val content: ContentSource,
    val images: ImagesSource,
    val volume: VolumeSource,
    val user: UserSource
)