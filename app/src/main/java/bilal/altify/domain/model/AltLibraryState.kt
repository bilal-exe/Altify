package bilal.altify.domain.model

data class AltLibraryState(
    val uri: String,
    val isAdded: Boolean = false,
    val canAdd: Boolean = false
)
