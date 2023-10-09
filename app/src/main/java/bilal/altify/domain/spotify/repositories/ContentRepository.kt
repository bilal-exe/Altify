package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.spotify.model.AltListItem
import bilal.altify.domain.spotify.model.AltListItems
import kotlinx.coroutines.flow.Flow

interface ContentRepository {

    val listItemsFlow: Flow<AltListItems>

    fun getRecommended()

    fun getChildrenOfItem(listItem: AltListItem, count: Int)

    fun loadMoreChildrenOfItem(listItem: AltListItem, offset: Int, count: Int)

    fun play(listItem: AltListItem)

    class ContentSourceException(override val message: String?) : Exception(message)

}