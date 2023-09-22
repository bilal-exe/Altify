package bilal.altify.domain.sources

import bilal.altify.domain.model.AltListItem
import com.spotify.protocol.types.ListItem
import kotlinx.coroutines.flow.Flow

interface ContentSource {

    val listItemsFlow: Flow<List<AltListItem>>

    fun getRecommended()

    fun getChildrenOfItem(listItem: AltListItem)

    fun play(listItem: ListItem)

    class ContentSourceException(override val message: String?): Exception(message)

}