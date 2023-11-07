package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.spotify.model.ListItem
import bilal.altify.domain.spotify.model.ListItems
import kotlinx.coroutines.flow.Flow

interface ContentRepository {

    val listItemsFlow: Flow<ListItems>

    fun getRecommended()

    fun getChildrenOfItem(listItem: ListItem, count: Int)

    fun loadMoreChildrenOfItem(listItem: ListItem, offset: Int, count: Int)

    fun play(listItem: ListItem)

    class ContentSourceException(override val message: String?) : Exception(message)

}