package bilal.altify.domain.model

data class LibraryState(
    val remoteId: RemoteId,
    val isAdded: Boolean,
    val canAdd: Boolean
)
