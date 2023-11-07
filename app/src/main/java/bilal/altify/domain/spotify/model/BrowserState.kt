package bilal.altify.domain.spotify.model

import android.graphics.Bitmap
import java.util.Stack

data class BrowserState(
    val listItems: AltListItems = AltListItems(),
    val libraryState: Map<String, AltLibraryState> = emptyMap(),
    val thumbnailMap: Map<String, Bitmap> = emptyMap(),
    val uriHistory: Stack<String> = Stack()
)