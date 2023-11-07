package bilal.altify.domain.spotify.use_case.model

import android.graphics.Bitmap
import bilal.altify.domain.spotify.model.LibraryState
import bilal.altify.domain.spotify.model.ListItems
import java.util.Stack

data class BrowserState(
    val listItems: ListItems = ListItems(),
    val libraryState: Map<String, LibraryState> = emptyMap(),
    val thumbnailMap: Map<String, Bitmap> = emptyMap(),
    val uriHistory: Stack<String> = Stack()
)