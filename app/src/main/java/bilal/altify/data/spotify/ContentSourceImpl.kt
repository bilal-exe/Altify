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

class ContentSourceImpl(
    private val contentApi: ContentApi
) : ContentSource {

    private fun listItemsCallback(lis: ListItems) {
        _listItemsFlow.value = lis.items.map { it.toAlt() }
    }

    private val _listItemsFlow = MutableStateFlow<List<AltListItem>>(emptyList())
    override val listItemsFlow = _listItemsFlow.asStateFlow()

    override fun getRecommended() {
        contentApi
            .getRecommendedContentItems(ContentApi.ContentType.DEFAULT)
            .setResultCallback(::listItemsCallback)
            .setErrorCallback { throw Exception("Error callback") }
    }

    override fun getChildrenOfItem(listItem: AltListItem) {
        contentApi
            .getChildrenOfItem(listItem.toOriginal(), 25, 0)
            .setResultCallback(::listItemsCallback)
            .setErrorCallback { throw Exception("Error callback") }
    }

    override fun play(listItem: ListItem) {
        contentApi.playContentItem(listItem)
    }

}