package bilal.altify.data.spotify

import bilal.altify.data.spotify.mappers.toAlt
import bilal.altify.data.spotify.mappers.toOriginal
import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.sources.ContentSource
import com.spotify.android.appremote.api.ContentApi
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.ListItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Stack

class ContentSourceImpl(
    private val contentApi: ContentApi
) : ContentSource {

    private val visitedHistory: Stack<AltListItem> = Stack()

    private fun listItemsCallback(lis: ListItems) {
        _listItemsFlow.value = lis.items.map { it.toAlt() }
    }

    private val _listItemsFlow = MutableStateFlow<List<AltListItem>>(emptyList())
    override val listItemsFlow = _listItemsFlow.asStateFlow()

    override fun getRecommended() {
        contentApi
            .getRecommendedContentItems(ContentApi.ContentType.DEFAULT)
            .setResultCallback(::listItemsCallback)
            .setErrorCallback { throw ContentSource.ContentSourceException(it.localizedMessage) }
    }

    override fun getChildrenOfItem(listItem: AltListItem) {
        visitedHistory.add(listItem)
        contentApi
            .getChildrenOfItem(listItem.toOriginal(), 25, 0)
            .setResultCallback(::listItemsCallback)
            .setErrorCallback { throw ContentSource.ContentSourceException(it.localizedMessage) }
    }

    override fun getPrevious() {
        when (visitedHistory.size) {
            0 ->
                getRecommended()
            1 -> {
                visitedHistory.pop()
                getRecommended()
            }
            else -> {
                visitedHistory.pop()
                getChildrenOfItem(visitedHistory.pop())
            }
        }
    }

    override fun play(listItem: ListItem) {
        contentApi.playContentItem(listItem)
    }

}