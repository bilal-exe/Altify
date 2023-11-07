package bilal.altify.data.spotify.repositories

import bilal.altify.data.mappers.SpotifyListItems
import bilal.altify.data.mappers.toModel
import bilal.altify.data.mappers.toOriginal
import bilal.altify.domain.model.MediaItem
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

    private val _listItemsFlow = MutableStateFlow(TODO())
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

    override fun getChildrenOfItem(mediaItem: MediaItem, count: Int) {
        contentApi
            .getChildrenOfItem(mediaItem.toOriginal(), count, 0)
            .setResultCallback(::listItemsCallback)
            .setErrorCallback { throw ContentRepository.ContentSourceException(it.localizedMessage) }
    }

    override fun loadMoreChildrenOfItem(mediaItem: MediaItem, offset: Int, count: Int) {
        contentApi
            .getChildrenOfItem(mediaItem.toOriginal(), count, offset)
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

    override fun play(mediaItem: MediaItem) {
        contentApi.playContentItem(mediaItem.toOriginal())
    }

}