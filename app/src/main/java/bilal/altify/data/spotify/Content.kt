package bilal.altify.data.spotify

import com.spotify.android.appremote.api.ContentApi
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.ListItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class Content(
    private val contentApi: ContentApi
) {

    private lateinit var listItemsCallback: (ListItems) -> Unit

    val listItemsFlow = callbackFlow {
        listItemsCallback = { trySend(it.items.map { it.toAlt() }) }
        awaitClose { this.cancel() }
    }
        .flowOn(IO)
        .stateIn(CoroutineScope(IO), SharingStarted.WhileSubscribed(), null)

    fun getRecommended() {
        contentApi
            .getRecommendedContentItems(ContentApi.ContentType.DEFAULT)
            .setResultCallback(listItemsCallback)
            .setErrorCallback { throw Exception("Error callback") }
    }

    fun getChildrenOfItem(listItem: ListItem) {
        contentApi
            .getChildrenOfItem(listItem, 25, 0)
            .setResultCallback(listItemsCallback)
            .setErrorCallback { throw Exception("Error callback") }
    }

    fun play(listItem: ListItem) {
        contentApi.playContentItem(listItem)
    }

}