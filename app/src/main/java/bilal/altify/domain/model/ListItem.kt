package bilal.altify.domain.model

data class ListItem(
    val uri: String,
    val imageUri: String,
    val title: String,
    val subtitle: String,
    val playable: Boolean,
    val type: ContentType
) {

    enum class ContentType {
        Section,
        Album,
        Artist,
        Playlist,
        Track,
    }

    val id: Int = countId++
    companion object {
        private var countId = 0
    }
}
