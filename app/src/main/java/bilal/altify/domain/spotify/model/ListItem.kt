package bilal.altify.domain.spotify.model

// the unique id exists for the lazy list, as duplicates in lists will crash the lazy list
// when keying on uri
data class ListItem(
    val id: Int = countId,
    val uri: String,
    val imageUri: String?,
    val title: String,
    val subtitle: String,
    val playable: Boolean,
    val hasChildren: Boolean,
    val type: ContentType = ContentType.Track
) {
    companion object {
        private var countId = 0
            get() = field++
    }
}

data class ListItems(
    val items: List<ListItem> = emptyList(),
    val total: Int = 0
) {
    operator fun invoke() = items
}


enum class ContentType {
    Section, Album, Artist, Playlist, Track,
}
