package bilal.altify.domain.model

data class PlayerContext(
    val uri: String,
    val title: String,
    val subtitle: String,
    val type: String
) {
    companion object {
        val example = PlayerContext("", "Title", "Subtitle", "Type")
    }
}
