package bilal.altify.domain.sources

import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.model.AltListItems
import com.spotify.protocol.types.ListItem
import kotlinx.coroutines.flow.Flow

interface ContentSource {

    val listItemsFlow: Flow<AltListItems>

    fun getRecommended()

    fun getChildrenOfItem(listItem: AltListItem, count: Int)

    fun loadMoreChildrenOfItem(listItem: AltListItem, offset: Int, count: Int)

    fun play(listItem: ListItem)

    class ContentSourceException(override val message: String?) : Exception(message)

}