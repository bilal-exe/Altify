package bilal.altify.domain.spotify.model

data class LibraryState(
    val uri: String,
    val isAdded: Boolean,
    val canAdd: Boolean
)
