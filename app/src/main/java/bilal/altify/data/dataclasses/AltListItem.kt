package bilal.altify.data.dataclasses

import com.spotify.protocol.types.ImageUri

data class AltListItem(
    val id: String,
    val uri: String,
    val imageUri: ImageUri,
    val title: String,
    val subtitle: String,
    val playable: Boolean,
    val hasChildren: Boolean
)
