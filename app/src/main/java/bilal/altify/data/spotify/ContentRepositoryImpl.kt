package bilal.altify.data.spotify

import bilal.altify.data.spotify.mappers.toAlt
import bilal.altify.data.spotify.mappers.toOriginal
import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.repository.ContentRepository
import com.spotify.android.appremote.api.ContentApi
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.ListItems
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class ContentRepositoryImpl(
    private val contentApi: ContentApi
) : ContentRepository {

    private lateinit var listItemsCallback: (ListItems) -> Unit

    override fun getListItems() =
        callbackFlow {

            trySend(null)

            listItemsCallback = { li ->
                trySend(
                    li.items.map { it.toAlt() }
                )
            }

            awaitClose { this.cancel() }

        }.flowOn(IO)

    override fun getRecommended() {
        contentApi
            .getRecommendedContentItems(ContentApi.ContentType.DEFAULT)
            .setResultCallback(listItemsCallback)
            .setErrorCallback { throw Exception("Error callback") }
    }

    override fun getChildrenOfItem(listItem: AltListItem) {
        contentApi
            .getChildrenOfItem(listItem.toOriginal(), 25, 0)
            .setResultCallback(listItemsCallback)
            .setErrorCallback { throw Exception("Error callback") }
    }

    override fun play(listItem: ListItem) {
        contentApi.playContentItem(listItem)
    }

}