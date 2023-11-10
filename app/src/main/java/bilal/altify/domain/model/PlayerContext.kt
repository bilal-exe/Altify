package bilal.altify.domain.model

data class PlayerContext(
    val remoteId: RemoteId,
    val title: String,
    val subtitle: String,
    val type: String
) {
    companion object {
        val example = PlayerContext(RemoteId.fake, "Title", "Subtitle", "Type")
    }
}
