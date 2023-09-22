package bilal.altify.domain.sources

import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.model.AltListItems
import kotlinx.coroutines.flow.Flow

interface ContentSource {

    val listItemsFlow: Flow<AltListItems>

    fun getRecommended()

    fun getChildrenOfItem(listItem: AltListItem, count: Int)

    fun loadMoreChildrenOfItem(listItem: AltListItem, offset: Int, count: Int)

    fun play(listItem: AltListItem)

    class ContentSourceException(override val message: String?) : Exception(message)

}