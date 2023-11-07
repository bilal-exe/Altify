package bilal.altify.domain.spotify.repositories.appremote

import bilal.altify.domain.model.MediaItem
import bilal.altify.domain.model.MediaItemsList
import kotlinx.coroutines.flow.Flow

interface ContentRepository {

    val listItemsFlow: Flow<MediaItemsList>

    fun getRecommended()

    fun getChildrenOfItem(mediaItem: MediaItem, count: Int)

    fun loadMoreChildrenOfItem(mediaItem: MediaItem, offset: Int, count: Int)

    fun play(mediaItem: MediaItem)

    class ContentSourceException(override val message: String?) : Exception(message)

}