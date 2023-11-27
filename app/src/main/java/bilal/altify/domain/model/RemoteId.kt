package bilal.altify.domain.model

// represents the ID for the item
data class RemoteId(
    val id: String,
    val contentType: ContentType
) {

    constructor(remoteId: String, contentTypeString: String) :
            this(remoteId, stringToContentType[contentTypeString]!!)

    init {
        if (!id.matches(regex)) throw InvalidRemoteIdException(id)
    }

    fun getContentTypeString() =
        contentTypeToString[this.contentType]!!

    companion object {
        private val regex = "^[a-zA-Z0-9]*$".toRegex()
        private val stringToContentType = mapOf(
            "artist" to ContentType.Artist,
            "playlist" to ContentType.Playlist,
            "track" to ContentType.Track,
            "section" to ContentType.Section,
            "album" to ContentType.Album,
        )
        private val contentTypeToString = stringToContentType.entries
            .associate { (k, v) -> v to k }
        val fake = RemoteId("", ContentType.Track)
    }
}

data class ImageRemoteId(
    val remoteId: String
)

enum class ContentType {
    Section,
    Album,
    Artist,
    Playlist,
    Track,
}

class InvalidRemoteIdException(val remoteId: String) :
    Exception("Tried to declare $remoteId as a RemoteId")

fun List<RemoteId>.toIdsList() =
    this.map { it.id }