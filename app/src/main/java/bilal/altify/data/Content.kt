package bilal.altify.data

import com.spotify.android.appremote.api.ContentApi
import com.spotify.protocol.client.CallResult
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.ListItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class Content(
    private val contentApi: ContentApi
) {

    private val scope = CoroutineScope(IO)
    private lateinit var listItemsCallback: CallResult.ResultCallback<ListItems>

    val listItemsFlow = callbackFlow {
        listItemsCallback = CallResult.ResultCallback<ListItems> { trySend(it) }
        awaitClose { this.cancel() }
    }
        .flowOn(IO)

    fun getRecommended() {
        scope.launch {
            contentApi
                .getRecommendedContentItems(ContentApi.ContentType.DEFAULT)
                .setResultCallback(listItemsCallback)
        }
    }

    fun getChildrenOfItem(listItem: ListItem) {
        scope.launch { contentApi.getChildrenOfItem(listItem, 25, 0) }
    }

    fun play(listItem: ListItem) {
        scope.launch { contentApi.playContentItem(listItem) }
    }

}