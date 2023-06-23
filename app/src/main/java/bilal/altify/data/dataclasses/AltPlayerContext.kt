package bilal.altify.data.dataclasses

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
