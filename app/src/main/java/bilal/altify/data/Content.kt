package bilal.altify.data

import com.spotify.android.appremote.api.ContentApi
import com.spotify.protocol.client.CallResult
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.ListItems
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class Content(
    private val contentApi: ContentApi
) {

    private lateinit var listItemsCallback: CallResult.ResultCallback<ListItems>

    val listItemsFlow = callbackFlow {
        listItemsCallback = CallResult.ResultCallback<ListItems> { trySend(it) }
        awaitClose { this.cancel() }
    }
        .flowOn(IO)

    suspend fun getRecommended() {
        contentApi
            .getRecommendedContentItems(ContentApi.ContentType.DEFAULT)
            .setResultCallback(listItemsCallback)
    }

    suspend fun getChildrenOfItem(listItem: ListItem) {
        contentApi.getChildrenOfItem(listItem, 25, 0)
    }

    suspend fun play(listItem: ListItem) {
         contentApi.playContentItem(listItem)
    }

}