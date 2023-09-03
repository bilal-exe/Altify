package bilal.altify.domain.model

import android.graphics.Bitmap

data class AltListItem(
    val id: String,
    val uri: String,
    val imageUri: String?,
    val title: String,
    val subtitle: String,
    val playable: Boolean,
    val hasChildren: Boolean,
    val thumbnail: Bitmap? = null
)
