package bilal.altify.data.spotify.repositories

import bilal.altify.data.spotify.mappers.toAlt
import bilal.altify.data.spotify.mappers.toOriginal
import bilal.altify.domain.spotify.model.AltListItem
import bilal.altify.domain.spotify.model.AltListItems
import bilal.altify.domain.spotify.repositories.ContentRepository
import com.spotify.android.appremote.api.ContentApi
import com.spotify.protocol.types.ListItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContentRepositoryImpl(
    private val contentApi: ContentApi
) : ContentRepository {

    private fun listItemsCallback(lis: ListItems) {
        _listItemsFlow.value = lis.toAlt()
    }

    private val _listItemsFlow = MutableStateFlow(AltListItems())
    override val listItemsFlow = _listItemsFlow.asStateFlow()

    init {
        getRecommended()
    }

    override fun getRecommended() {
        contentApi
            .getRecommendedContentItems(ContentApi.ContentType.DEFAULT)
            .setResultCallback(::listItemsCallback)
            .setErrorCallback { throw ContentRepository.ContentSourceException(it.localizedMessage) }
    }

    override fun getChildrenOfItem(listItem: AltListItem, count: Int) {
        contentApi
            .getChildrenOfItem(listItem.toOriginal(), count, 0)
            .setResultCallback(::listItemsCallback)
            .setErrorCallback { throw ContentRepository.ContentSourceException(it.localizedMessage) }
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
            .setErrorCallback { throw ContentRepository.ContentSourceException(it.localizedMessage) }
    }

    override fun play(listItem: AltListItem) {
        contentApi.playContentItem(listItem.toOriginal())
    }

}