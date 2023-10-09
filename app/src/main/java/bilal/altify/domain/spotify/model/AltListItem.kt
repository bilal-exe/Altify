package bilal.altify.domain.spotify.model

// the unique id exists for the lazy list, as duplicates in lists will crash the lazy list
// when keying on uri
data class AltListItem(
    val id: Int = countId,
    val uri: String,
    val imageUri: String?,
    val title: String,
    val subtitle: String,
    val playable: Boolean,
    val hasChildren: Boolean
) {
    companion object {
        private var countId = 0
            get() = field++
    }
}

data class AltListItems(
    val items: List<AltListItem> = emptyList(),
    val total: Int = 0
) {
    operator fun invoke() = items
}
