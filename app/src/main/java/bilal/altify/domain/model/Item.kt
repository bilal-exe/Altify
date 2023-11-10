package bilal.altify.domain.model

data class ListItem(
    val remoteId: RemoteId,
    val imageRemoteId: ImageRemoteId?,
    val title: String,
    val subtitle: String,
    val playable: Boolean,
) {
    val id: Int = countId++

    companion object {
        private var countId = 0
    }
}

/*
* The types of content available to browse or play
* */
data class ListItems(
    val total: Int,
    val items: List<ListItem>
)

sealed interface Item {

    val remoteId: RemoteId
    val name: String

    sealed interface Album : Item {
        override val remoteId: RemoteId
        override val name: String
    }

    sealed interface Artist : Item {
        override val remoteId: RemoteId
        override val name: String
    }

    sealed interface Playlist : Item {
        override val remoteId: RemoteId
        override val name: String
    }

    sealed interface Track : Item {
        override val remoteId: RemoteId
        override val name: String
        val artist: Artist
        val artists: List<Artist>
        val album: Album
        val duration: Long
        val imageId: ImageRemoteId?
    }

}




