package bilal.altify.presentation.screens.home.browse

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bilal.altify.R
import bilal.altify.domain.model.ContentType
import bilal.altify.domain.model.ImageRemoteId
import bilal.altify.domain.model.LibraryState
import bilal.altify.domain.model.ListItems
import bilal.altify.domain.model.ListItem
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.spotify.use_case.model.Command
import bilal.altify.domain.spotify.use_case.model.ContentCommand
import bilal.altify.domain.spotify.use_case.model.PlaybackCommand
import bilal.altify.domain.spotify.use_case.model.UserCommand
import bilal.altify.presentation.util.ShakeBounceAnimation
import bilal.altify.presentation.util.clipLen
import com.spotify.protocol.types.Image.Dimension

fun LazyListScope.browserItemsList(
    listItems: ListItems,
    playingTrackId: RemoteId?,
    thumbnailMap: Map<ImageRemoteId, Bitmap>,
    libraryState: Map<RemoteId, LibraryState>,
    executeCommand: (Command) -> Unit,
    backgroundColor: Color,
) {

    val getRecommended: () -> Unit = {
        executeCommand(ContentCommand.GetRecommended)
    }
    val playItem: (ListItem, Int) -> Unit = { item, index ->
        val command = when (item.remoteId.contentType) {
            ContentType.Track -> PlaybackCommand.SkipToTrack(item.remoteId, index)
            else -> ContentCommand.Play(item)
        }
        executeCommand(command)
    }
    val getChildrenOfItem: (ListItem) -> Unit = {
        executeCommand(ContentCommand.GetChildrenOfItem(it))
    }
    val toggleLibraryStatus: (RemoteId, Boolean) -> Unit = { uri, added ->
        executeCommand(UserCommand.ToggleLibraryStatus(uri, added))
    }
    val addToQueue: (RemoteId) -> Unit = {
        executeCommand(PlaybackCommand.AddToQueue(it))
    }

//    var newListLoading by remember { mutableStateOf(false) }
//    LaunchedEffect(key1 = listItems) {
//        newListLoading = false
//    }
//

    item {
        GetRecommendedButton(getRecommended)
    }
    items(
        items = listItems.items,
        key = { it.id }
    ) { item ->
        ListItemRow(
            item = item,
            selected = playingTrackId == item.remoteId,
            thumbnail = thumbnailMap[item.imageRemoteId],
            playItem = { playItem(item, listItems.items.indexOf(item)) },
            getChildrenOfItem = { getChildrenOfItem(item) },
            libraryState = libraryState[item.remoteId],
            toggleLibraryStatus = toggleLibraryStatus,
            addToQueue = addToQueue,
            backgroundColor = backgroundColor
        )
    }
    if (listItems.items.size < listItems.total) item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun GetRecommendedButton(getRecommended: () -> Unit) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(16.dp)),
        onClick = { getRecommended() },
        colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = "Get Recommended Items",
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun ListItemRow(
    item: ListItem,
    selected: Boolean,
    thumbnail: Bitmap?,
    playItem: () -> Unit,
    getChildrenOfItem: () -> Unit,
    libraryState: LibraryState?,
    toggleLibraryStatus: (RemoteId, Boolean) -> Unit,
    addToQueue: (RemoteId) -> Unit,
    backgroundColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .height((144 / LocalDensity.current.density).dp),
    ) {
        // only tracks can be queued
        if (item.remoteId.contentType == ContentType.Track) SwipeableListItemRowContent(
            item = item,
            selected = selected,
            thumbnail = thumbnail,
            playItem = playItem,
            getChildrenOfItem = getChildrenOfItem,
            libraryState = libraryState,
            toggleLibraryStatus = toggleLibraryStatus,
            addToQueue = addToQueue,
            backgroundColor = backgroundColor,
        ) else ListItemRowContent(
            item = item,
            selected = selected,
            thumbnail = thumbnail,
            playItem = playItem,
            getChildrenOfItem = getChildrenOfItem,
            libraryState = libraryState,
            toggleLibraryStatus = toggleLibraryStatus,
            backgroundColor = backgroundColor,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableListItemRowContent(
    item: ListItem,
    selected: Boolean,
    thumbnail: Bitmap?,
    playItem: () -> Unit,
    getChildrenOfItem: () -> Unit,
    libraryState: LibraryState?,
    toggleLibraryStatus: (RemoteId, Boolean) -> Unit,
    addToQueue: (RemoteId) -> Unit,
    backgroundColor: Color,
) {
    val view = LocalView.current
    val config = LocalConfiguration.current
    val density = LocalDensity.current
    val dismissThreshold = remember {
        with(density) { config.screenWidthDp.times(0.4f).dp.toPx() }
    }
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToEnd) {
                addToQueue(item.remoteId)
                view.performHapticFeedback(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.CONFIRM
                    else 16
                )
            }
            it != DismissValue.DismissedToEnd
        },
        positionalThreshold = { dismissThreshold }
    )


    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd),
        background = {
            dismissState.dismissDirection ?: return@SwipeToDismiss
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1.25f, label = ""
            )
            LaunchedEffect(dismissState.progress > 0.4f) {
                if (dismissState.progress == 1f) return@LaunchedEffect // bug where the val is 1
                view.performHapticFeedback(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.CONFIRM
                    else 16
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.queue_music),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .scale(scale),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        dismissContent = {
            ListItemRowContent(
                item = item,
                selected = selected,
                thumbnail = thumbnail,
                playItem = playItem,
                getChildrenOfItem = getChildrenOfItem,
                libraryState = libraryState,
                toggleLibraryStatus = toggleLibraryStatus,
                backgroundColor = backgroundColor,
            )
        },
    )
}

@Composable
fun ListItemRowContent(
    item: ListItem,
    selected: Boolean,
    thumbnail: Bitmap?,
    playItem: () -> Unit,
    getChildrenOfItem: () -> Unit,
    libraryState: LibraryState?,
    toggleLibraryStatus: (RemoteId, Boolean) -> Unit,
    backgroundColor: Color,
) {
    val rowHeight = with(LocalDensity.current) { Dimension.THUMBNAIL.value.toDp() }
    val thumbnailModifier = Modifier
        .clip(RoundedCornerShape(6.dp))
        .width(rowHeight)
        .aspectRatio(1f)

    Row(
        modifier = Modifier
            .background(
                color = if (selected) backgroundColor.copy(alpha = 0.25f) else backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = getChildrenOfItem),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (thumbnail != null) ItemThumbnail(thumbnail, thumbnailModifier)
            else PlaceholderThumbnail(thumbnailModifier)
            Row(
                modifier = Modifier
                    .width(
                        LocalConfiguration.current.screenWidthDp.dp -
                                (3 * rowHeight.value).dp -
                                16.dp -
                                24.dp
                    )
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                ListItemInfo(
                    title = item.title, subtitle = item.subtitle, modifier = Modifier
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (libraryState != null) AddRemoveLibraryIcon(
            libraryState = libraryState,
            toggleLibraryStatus = toggleLibraryStatus,
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
    libraryState: LibraryState,
    modifier: Modifier = Modifier,
    toggleLibraryStatus: (RemoteId, Boolean) -> Unit,
) {
    val icon = when (libraryState.isAdded) {
        true -> Icons.Default.Favorite
        false -> Icons.Outlined.FavoriteBorder
    }
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        if (libraryState.canAdd) {
            ShakeBounceAnimation(
                icon = icon,
                modifier = Modifier
                    .clickable {
                        toggleLibraryStatus(libraryState.remoteId, !libraryState.isAdded)
                    },
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                )
            }
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
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        if (playable) Icon(
            painter = painterResource(id = R.drawable.play),
            contentDescription = "",
            modifier = Modifier.clickable { playItem() },
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
        Text(
            text = title,
            fontSize = 22.sp,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        if (subtitle.isNotBlank()) Text(
            text = subtitle.clipLen(40),
            fontStyle = MaterialTheme.typography.labelMedium.fontStyle,
            fontSize = 18.sp,
            maxLines = 1
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
        item = ListItem(
            remoteId = RemoteId.fake,
            imageRemoteId = ImageRemoteId(""),
            title = "Title",
            subtitle = "Subtitle",
            playable = true,
        ),
        selected = false,
        thumbnail = null,
        playItem = {},
        getChildrenOfItem = {},
        libraryState = LibraryState(remoteId = RemoteId.fake, isAdded = true, canAdd = true),
        toggleLibraryStatus = { _, _ -> },
        addToQueue = {},
        backgroundColor = MaterialTheme.colorScheme.background
    )
}

@Preview(showBackground = true)
@Composable
fun ItemsListPreview() {
    val items = mutableListOf<ListItem>()
    val a = RemoteId.fake.copy(id = "a")
    repeat(5) {
        val alter = it % 2 == 0
        val ali = ListItem(
            remoteId = if (alter) RemoteId.fake else RemoteId.fake.copy(id = "a"),
            imageRemoteId = ImageRemoteId(""),
            title = if (alter) "Title" else "TitleTitleTitleTitleTitleTitleTitle",
            subtitle = "Subtitle",
            playable = alter,
        )
        items.add(ali)
    }
    val backgroundColor = MaterialTheme.colorScheme.background
    LazyColumn {
        browserItemsList(
            listItems = ListItems(5, items),
            playingTrackId = null,
            thumbnailMap = emptyMap(),
            libraryState = mapOf(a to LibraryState(a, isAdded = true, canAdd = true)),
            executeCommand = { },
            backgroundColor = backgroundColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemsListPreview2() {
    val items = mutableListOf<ListItem>()
    val a = RemoteId.fake.copy(id = "a")
    repeat(5) {
        val alter = it % 2 == 0
        val ali = ListItem(
            remoteId = if (alter) RemoteId.fake else a,
            imageRemoteId = ImageRemoteId(""),
            title = if (alter) "Title" else "TitleTitleTitleTitleTitleTitleTitle",
            subtitle = "Subtitle",
            playable = alter,
        )
        items.add(ali)
    }
    val backgroundColor = MaterialTheme.colorScheme.background
    LazyColumn {
        browserItemsList(
            listItems = ListItems(items = items, total = items.size + 10),
            playingTrackId = null,
            thumbnailMap = emptyMap(),
            libraryState = mapOf(a to LibraryState(remoteId = a, isAdded = true, canAdd = true)),
            executeCommand = { },
            backgroundColor = backgroundColor,
        )
    }
}