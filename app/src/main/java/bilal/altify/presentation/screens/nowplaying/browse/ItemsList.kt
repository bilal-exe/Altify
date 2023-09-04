package bilal.altify.presentation.screens.nowplaying.browse

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bilal.altify.R
import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.model.AltTrack
import bilal.altify.presentation.screens.nowplaying.BROWSER_FAB_HEIGHT
import bilal.altify.presentation.screens.nowplaying.complementColor
import bilal.altify.presentation.util.AltText
import bilal.altify.presentation.util.clipLen
import com.spotify.protocol.types.Image.Dimension
import kotlin.math.ceil


@Composable
fun ItemsList(
    listItems: List<AltListItem>,
    track: AltTrack?,
    playItem: (AltListItem) -> Unit,
    getChildrenOfItem: (AltListItem) -> Unit,
    thumbnailMap: Map<String, Bitmap>
) {
    LazyColumn(
//         calculates the height of the list by the thumbnail height plus padding for each list item
//         allows this lazy column to sit in a scrollable without causing an IllegalStateException for nesting
        modifier = Modifier.height(
            (listItems.size * ceil((144 / LocalDensity.current.density) + 16)).dp
                    + 16.dp // extra room
                    + BROWSER_FAB_HEIGHT.dp // so floating action button doesn't overlap
        ),
        userScrollEnabled = false
    ) {
        items(
            items = listItems,
            key = { it.id }
        ) { item ->
            ListItemRow(
                item = item,
                selected = track?.uri == item.uri,
                thumbnail = thumbnailMap[item.imageUri],
                playItem = { playItem(item) },
                getChildrenOfItem = { getChildrenOfItem(item) }
            )
        }
    }
}

@Composable
fun ListItemRow(
    item: AltListItem,
    selected: Boolean,
    thumbnail: Bitmap?,
    playItem: () -> Unit,
    getChildrenOfItem: () -> Unit,
) {

    val listItemModifier = Modifier
        .width(with(LocalDensity.current) { Dimension.THUMBNAIL.value.toDp() })
        .clip(RoundedCornerShape(6.dp))
        .background(Color.Gray)
        .aspectRatio(1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .background(
                color = if (selected) Color.LightGray.copy(alpha = 0.25f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clickable { getChildrenOfItem() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (thumbnail != null) ItemThumbnail(thumbnail, listItemModifier)
            else PlaceholderThumbnail(listItemModifier)
            Spacer(modifier = Modifier.width(16.dp))
            ListItemInfo(title = item.title, subtitle = item.subtitle)
        }
        Spacer(modifier = Modifier.weight(1f))
        if (item.playable) PlayButton(playItem)
    }
}

@Composable
fun PlayButton(playItem: () -> Unit) {
    Icon(
        painter = painterResource(id = R.drawable.play),
        contentDescription = "",
        modifier = Modifier.clickable { playItem() },
        tint = complementColor
    )
}

@Composable
fun ListItemInfo(title: String, subtitle: String) {
    Column {
        AltText(
            text = title,
            fontSize = 22.sp,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            fontWeight = FontWeight.Bold
        )
        if (subtitle.isNotBlank())
            AltText(
                text = subtitle.clipLen(40),
                fontStyle = MaterialTheme.typography.labelMedium.fontStyle,
                fontSize = 18.sp
            )
    }
}


@Composable
fun ItemThumbnail(thumbnail: Bitmap, modifier: Modifier) {
    Image(
        modifier = modifier,
        bitmap = thumbnail.asImageBitmap(),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
    )
}

@Composable
fun PlaceholderThumbnail(modifier: Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.music),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
    )
}

@Preview(showBackground = true)
@Composable
private fun ListItemRowPreview() {
    ListItemRow(
        item = AltListItem(
            id = "",
            uri = "",
            imageUri = "",
            title = "Title",
            subtitle = "Subtitle",
            playable = true,
            hasChildren = true
        ),
        selected = false,
        thumbnail = null,
        playItem = {},
        getChildrenOfItem = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ItemsListPreview() {
    val items = mutableListOf<AltListItem>()
    repeat(5) {
        val ali = AltListItem(
            id = it.toString(),
            uri = "",
            imageUri = "",
            title = "Title",
            subtitle = "Subtitle",
            playable = it % 2 == 0,
            hasChildren = true
        )
        items.add(ali)
    }
    ItemsList(
        listItems = items,
        track = null,
        playItem = {},
        getChildrenOfItem = {},
        thumbnailMap = emptyMap()
    )
}