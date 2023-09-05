package bilal.altify.presentation.screens.nowplaying.browse

import android.graphics.Bitmap
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bilal.altify.R
import bilal.altify.domain.model.AltLibraryState
import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.model.AltTrack
import bilal.altify.presentation.screens.nowplaying.BROWSER_FAB_HEIGHT
import bilal.altify.presentation.screens.nowplaying.bodyColor
import bilal.altify.presentation.screens.nowplaying.titleColor
import bilal.altify.presentation.util.AltText
import bilal.altify.presentation.util.UpdateEffect
import bilal.altify.presentation.util.clipLen
import bilal.altify.presentation.util.shakeShrinkAnimation
import com.spotify.protocol.types.Image.Dimension
import kotlin.math.ceil


@Composable
fun ItemsList(
    listItems: List<AltListItem>,
    track: AltTrack?,
    playItem: (AltListItem) -> Unit,
    getChildrenOfItem: (AltListItem) -> Unit,
    thumbnailMap: Map<String, Bitmap>,
    libraryState: Map<String, AltLibraryState>,
    addToLibrary: (String) -> Unit,
    removeFromLibrary: (String) -> Unit
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
                getChildrenOfItem = { getChildrenOfItem(item) },
                libraryState = libraryState[item.uri],
                addToLibrary = addToLibrary,
                removeFromLibrary = removeFromLibrary
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
    libraryState: AltLibraryState?,
    addToLibrary: (String) -> Unit,
    removeFromLibrary: (String) -> Unit,
) {
    val rowHeight = with(LocalDensity.current) { Dimension.THUMBNAIL.value.toDp() }
    val listItemModifier = Modifier
        .width(rowHeight)
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
        Box(
            modifier = Modifier.width(
                (LocalConfiguration.current.screenWidthDp - (24 + (16 + (2 * (rowHeight.value))))).dp
            )
        ) {
            Row(
                modifier = Modifier
                    .clickable { getChildrenOfItem() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (thumbnail != null) ItemThumbnail(thumbnail, listItemModifier)
                else PlaceholderThumbnail(listItemModifier)
                Spacer(modifier = Modifier.width(16.dp))
                ListItemInfo(
                    title = item.title,
                    subtitle = item.subtitle,
                    modifier = Modifier
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (libraryState != null) AddRemoveLibraryIcon(
            libraryState = libraryState,
            addToLibrary = addToLibrary,
            removeFromLibrary = removeFromLibrary,
            modifier = Modifier.size(rowHeight, rowHeight)
        )
        PlayButton(
            playItem = playItem,
            playable = item.playable,
            modifier = Modifier.size(rowHeight, rowHeight)
        )
    }
}

@Composable
fun AddRemoveLibraryIcon(
    libraryState: AltLibraryState,
    modifier: Modifier = Modifier,
    addToLibrary: (String) -> Unit,
    removeFromLibrary: (String) -> Unit
) {
    val icon = when (libraryState.isAdded) {
        true -> Icons.Default.Favorite
        false -> Icons.Outlined.FavoriteBorder
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (libraryState.canAdd) {
            val scale = remember { Animatable(1f) }
            val rotation = remember { Animatable(1f) }
            val coroutineScope = rememberCoroutineScope()

            UpdateEffect(libraryState.isAdded) {
                shakeShrinkAnimation(scale = scale, rotation = rotation, scope = coroutineScope)
            }

            Icon(
                imageVector = icon,
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        when (libraryState.isAdded) {
                            true -> removeFromLibrary(libraryState.uri)
                            false -> addToLibrary(libraryState.uri)
                        }
                    }
                    .scale(scale = scale.value)
                    .rotate(rotation.value)

            )
        }
    }
}

@Composable
fun PlayButton(
    playItem: () -> Unit,
    modifier: Modifier = Modifier,
    playable: Boolean
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (playable) Icon(
            painter = painterResource(id = R.drawable.play),
            contentDescription = "",
            modifier = Modifier.clickable { playItem() },
            tint = titleColor
        ) else Spacer(modifier = modifier)
    }
}

@Composable
fun ListItemInfo(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier) {
        AltText(
            text = title,
            fontSize = 22.sp,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            fontWeight = FontWeight.Bold
        )
        if (subtitle.isNotBlank())
            Text(
                text = subtitle.clipLen(40),
                fontStyle = MaterialTheme.typography.labelMedium.fontStyle,
                fontSize = 18.sp,
                color = bodyColor
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
        getChildrenOfItem = {},
        libraryState = AltLibraryState(uri = "", isAdded = true, canAdd = true),
        addToLibrary = {},
        removeFromLibrary = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ItemsListPreview() {
    val items = mutableListOf<AltListItem>()
    repeat(5) {
        val alter = it % 2 == 0
        val ali = AltListItem(
            id = it.toString(),
            uri = if (alter) "" else "a",
            imageUri = "",
            title = if (alter) "Title" else "TitleTitleTitleTitleTitleTitleTitle",
            subtitle = "Subtitle",
            playable = alter,
            hasChildren = alter
        )
        items.add(ali)
    }
    ItemsList(
        listItems = items,
        track = null,
        playItem = {},
        getChildrenOfItem = {},
        thumbnailMap = emptyMap(),
        libraryState = mapOf("a" to AltLibraryState(uri = "", isAdded = true, canAdd = true)),
        addToLibrary = {},
        removeFromLibrary = {}
    )
}