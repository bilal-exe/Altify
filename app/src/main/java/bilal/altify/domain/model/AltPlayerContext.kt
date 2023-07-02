package bilal.altify.domain.model

data class AltPlayerContext(
    val uri: String,
    val title: String,
    val subtitle: String,
    val type: String
) {
    companion object {
        val example = AltPlayerContext("", "Title", "Subtitle", "Type")
    }
}