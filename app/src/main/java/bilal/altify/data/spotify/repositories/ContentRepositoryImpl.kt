package bilal.altify.data.spotify.repositories

import android.util.Log
import bilal.altify.data.mappers.SpotifyListItems
import bilal.altify.data.mappers.toModel
import bilal.altify.data.mappers.toOriginal
import bilal.altify.domain.model.ListItem
import bilal.altify.domain.model.ListItems
import bilal.altify.domain.spotify.repositories.appremote.ContentRepository
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

    private val _listItemsFlow = MutableStateFlow<ListItems?>(null)
    override val listItemsFlow = _listItemsFlow.asStateFlow()

    init {
        getRecommended()
    }

    override fun getRecommended() {
        contentApi
            .getRecommendedContentItems(ContentApi.ContentType.DEFAULT)
            .setResultCallback(::listItemsCallback)
            .setErrorCallback {
                Log.d("Error", it.message.toString())
                throw ContentRepository.ContentSourceException(it.localizedMessage)
            }
    }

    override fun getChildrenOfItem(item: ListItem, count: Int) {
        contentApi
            .getChildrenOfItem(item.toOriginal(), count, 0)
            .setResultCallback(::listItemsCallback)
            .setErrorCallback {
                Log.d("Error", "${it.message.toString()} $item")
                throw ContentRepository.ContentSourceException(it.localizedMessage)
            }
    }

    override fun loadMoreChildrenOfItem(item: ListItem, offset: Int, count: Int) {
        contentApi
            .getChildrenOfItem(item.toOriginal(), count, offset)
            .setResultCallback { res -> _listItemsFlow.update {
                    it?.copy(
                        items = it.items + res.toModel().items,
                        total = res.total
                    )
                        ?: res.toModel()
            } }
            .setErrorCallback {
                Log.d("Error", "${it.message.toString()} $item")
                throw ContentRepository.ContentSourceException(it.localizedMessage)
            }
    }

    override fun play(item: ListItem) {
        contentApi.playContentItem(item.toOriginal())
    }

}