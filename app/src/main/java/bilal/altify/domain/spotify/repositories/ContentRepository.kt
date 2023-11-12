package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.model.ListItem
import bilal.altify.domain.model.ListItems
import kotlinx.coroutines.flow.Flow

interface ContentRepository {

    val listItemsFlow: Flow<ListItems?>

    fun getRecommended()

    fun getChildrenOfItem(item: ListItem, count: Int)

    fun loadMoreChildrenOfItem(item: ListItem, offset: Int, count: Int)

    fun play(item: ListItem)

    class ContentSourceException(override val message: String?) : Exception(message)

}