package bilal.altify.data.spotify.repositories

import bilal.altify.data.spotify.mappers.SpotifyListItems
import bilal.altify.data.spotify.mappers.toModel
import bilal.altify.data.spotify.mappers.toOriginal
import bilal.altify.domain.spotify.model.ListItem
import bilal.altify.domain.spotify.repositories.ContentRepository
import com.spotify.android.appremote.api.ContentApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContentRepositoryImpl(
    private val contentApi: ContentApi
) : ContentRepository {

    private fun listItemsCallback(lis: SpotifyListItems) {
        _listItemsFlow.value = lis.toModel()
    }

    private val _listItemsFlow = MutableStateFlow(bilal.altify.domain.spotify.model.ListItems())
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

    override fun getChildrenOfItem(listItem: ListItem, count: Int) {
        contentApi
            .getChildrenOfItem(listItem.toOriginal(), count, 0)
            .setResultCallback(::listItemsCallback)
            .setErrorCallback { throw ContentRepository.ContentSourceException(it.localizedMessage) }
    }

    override fun loadMoreChildrenOfItem(listItem: ListItem, offset: Int, count: Int) {
        contentApi
            .getChildrenOfItem(listItem.toOriginal(), count, offset)
            .setResultCallback { res ->
                _listItemsFlow.update {
                    it.copy(
                        items = it.items + res.toModel().items,
                        total = res.total
                    )
                }
            }
            .setErrorCallback { throw ContentRepository.ContentSourceException(it.localizedMessage) }
    }

    override fun play(listItem: ListItem) {
        contentApi.playContentItem(listItem.toOriginal())
    }

}