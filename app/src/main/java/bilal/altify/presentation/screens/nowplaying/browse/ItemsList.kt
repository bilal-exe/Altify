package bilal.altify.presentation.screens.nowplaying.browse

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import bilal.altify.R
import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.model.AltTrack
import com.spotify.protocol.types.Image.Dimension


@Composable
fun ItemsList(
    listItems: List<AltListItem>,
    track: AltTrack?,
    palette: Palette?,
    playItem: (AltListItem) -> Unit,
    getChildrenOfItem: (AltListItem) -> Unit,
    getThumbnail: (String) -> Unit,
    thumbnailMap: Map<String, Bitmap>
) {
    LazyColumn {
        items(
            items = listItems,
            key = { it.id },
        ) { item ->
            ListItemRow(
                item = item,
                selected = track?.uri == item.uri,
                thumbnail = thumbnailMap[item.imageUri]
            )
        }
    }
}

@Composable
fun ListItemRow(
    item: AltListItem,
    selected: Boolean,
    thumbnail: Bitmap?
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        if (thumbnail == null) PlaceholderThumbnail() else ItemThumbnail(thumbnail)
    }
}

@Composable
fun ItemThumbnail(thumbnail: Bitmap) {

}

@Composable
fun PlaceholderThumbnail() {
    Image(
        modifier = Modifier
            .width(with(LocalDensity.current) {  Dimension.THUMBNAIL.value.toDp() })
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Gray)
            .aspectRatio(1f),
        painter = painterResource(id = R.drawable.music),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
    )
}
