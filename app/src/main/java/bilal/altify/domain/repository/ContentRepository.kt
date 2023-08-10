package bilal.altify.domain.repository

import bilal.altify.domain.model.AltListItem
import com.spotify.protocol.types.ListItem
import kotlinx.coroutines.flow.Flow

interface ContentRepository {

    fun getListItemsFlow(): Flow<List<AltListItem>?>

    fun getRecommended()

    fun getChildrenOfItem(listItem: AltListItem)

    fun play(listItem: ListItem)

}