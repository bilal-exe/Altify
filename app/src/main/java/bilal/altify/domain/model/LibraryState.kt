package bilal.altify.domain.model

data class LibraryState(
    val uri: String,
    val isAdded: Boolean,
    val canAdd: Boolean
)
