package bilal.altify.data.spotify

import bilal.altify.data.spotify.mappers.toAlt
import bilal.altify.data.spotify.mappers.toOriginal
import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.model.AltListItems
import bilal.altify.domain.sources.ContentSource
import com.spotify.android.appremote.api.ContentApi
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.ListItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContentSourceImpl(
    private val contentApi: ContentApi
) : ContentSource {

    private fun listItemsCallback(lis: ListItems) {
        _listItemsFlow.value = lis.toAlt()
    }

    private val _listItemsFlow = MutableStateFlow(AltListItems())
    override val listItemsFlow = _listItemsFlow.asStateFlow()

    override fun getRecommended() {
        contentApi
            .getRecommendedContentItems(ContentApi.ContentType.DEFAULT)
            .setResultCallback(::listItemsCallback)
            .setErrorCallback { throw ContentSource.ContentSourceException(it.localizedMessage) }
    }

    override fun getChildrenOfItem(listItem: AltListItem, count: Int) {
        contentApi
            .getChildrenOfItem(listItem.toOriginal(), count, 0)
            .setResultCallback(::listItemsCallback)
            .setErrorCallback { throw ContentSource.ContentSourceException(it.localizedMessage) }
    }

    override fun loadMoreChildrenOfItem(listItem: AltListItem, offset: Int, count: Int) {
        contentApi
            .getChildrenOfItem(listItem.toOriginal(), count, offset)
            .setResultCallback { res ->
                _listItemsFlow.update {
                    it.copy(
                        items = it.items + res.toAlt().items,
                        total = res.total
                    )
                }
            }
            .setErrorCallback { throw ContentSource.ContentSourceException(it.localizedMessage) }
    }

    override fun play(listItem: ListItem) {
        contentApi.playContentItem(listItem)
    }

}