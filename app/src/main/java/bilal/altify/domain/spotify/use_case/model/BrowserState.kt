package bilal.altify.domain.spotify.use_case.model

import android.graphics.Bitmap
import bilal.altify.domain.model.ImageRemoteId
import bilal.altify.domain.model.LibraryState
import bilal.altify.domain.model.ListItems
import bilal.altify.domain.model.RemoteId
import java.util.Stack

data class BrowserState(
    val listItems: ListItems? = null,
    val libraryState: Map<RemoteId, LibraryState> = emptyMap(),
    val thumbnailMap: Map<ImageRemoteId, Bitmap> = emptyMap(),
    val uriHistory: Stack<RemoteId> = Stack()
)