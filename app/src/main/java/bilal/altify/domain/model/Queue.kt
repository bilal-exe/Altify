package bilal.altify.domain.model

data class Queue(
    val currentlyPlaying: ExtendedItem.Track,
    val queue: List<ExtendedItem.Track>
)
