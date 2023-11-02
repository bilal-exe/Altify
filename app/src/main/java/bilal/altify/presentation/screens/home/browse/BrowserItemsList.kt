package bilal.altify.presentation.screens.home.browse

import android.graphics.Bitmap
import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyListState
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
import bilal.altify.domain.spotify.model.AltLibraryState
import bilal.altify.domain.spotify.model.AltListItem
import bilal.altify.domain.spotify.model.AltListItems
import bilal.altify.domain.spotify.model.ContentType
import bilal.altify.domain.spotify.use_case.Command
import bilal.altify.domain.spotify.use_case.ContentCommand
import bilal.altify.domain.spotify.use_case.ImagesCommand
import bilal.altify.domain.spotify.use_case.PlaybackCommand
import bilal.altify.domain.spotify.use_case.UserCommand
import bilal.altify.presentation.util.ShakeBounceAnimation
import bilal.altify.presentation.util.clipLen
import com.spotify.protocol.types.Image.Dimension


fun LazyListScope.browserItemsList(
    listItems: AltListItems,
    track: String?,
    thumbnailMap: Map<String, Bitmap>,
    libraryState: Map<String, AltLibraryState>,
    executeCommand: (Command) -> Unit,
    backgroundColor: Color,
    lazyListState: LazyListState
) {

    val getRecommended: () -> Unit = {
        executeCommand(ContentCommand.GetRecommended)
    }
    val playItem: (AltListItem, Int) -> Unit = { item, index ->
        val command = when (item.type) {
            ContentType.Track -> PlaybackCommand.SkipToTrack(item.uri, index)
            else -> ContentCommand.Play(item)
        }
        executeCommand(command)
    }
    val getChildrenOfItem: (AltListItem) -> Unit = {
        executeCommand(ContentCommand.GetChildrenOfItem(it))
    }
    val getThumbnail: (String) -> Unit = {
        executeCommand(ImagesCommand.GetThumbnail(it))
    }
    val toggleLibraryStatus: (String, Boolean) -> Unit = { uri, added ->
        executeCommand(UserCommand.ToggleLibraryStatus(uri, added))
    }
    val addToQueue: (String) -> Unit = {
        executeCommand(PlaybackCommand.AddToQueue(it))
    }

//    var newListLoading by remember { mutableStateOf(false) }
//    LaunchedEffect(key1 = listItems) {
//        newListLoading = false
//    }
//

    item {

        GetRecommendedButton(getRecommended)

        BackHandler {
            executeCommand(ContentCommand.GetPrevious)
        }

        LaunchedEffect(key1 = listItems) {
            if (listItems().isNotEmpty()) {
                executeCommand(ImagesCommand.ClearThumbnails)
                listItems().forEach { item ->
                    if (!item.imageUri.isNullOrBlank()) getThumbnail(item.imageUri)
                }
                executeCommand(UserCommand.UpdateBrowserLibraryState(listItems().map { it.uri }))
            }
        }

        LaunchedEffect(key1 = lazyListState.canScrollForward) {
            if (!lazyListState.canScrollForward) {
                executeCommand(ContentCommand.LoadMoreChildrenOfItem(listItems))
            }
        }

        val showLoadingIndicator = remember(listItems.items) {
            listItems.items.size < listItems.total
        }

        if (showLoadingIndicator) Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

    }
    itemsIndexed(
        items = listItems(),
    ) { index, item ->
        ListItemRow(
            item = item,
            selected = track == item.uri,
            thumbnail = thumbnailMap[item.imageUri],
            playItem = { playItem(item, index) },
            getChildrenOfItem = { getChildrenOfItem(item) },
            libraryState = libraryState[item.uri],
            toggleLibraryStatus = toggleLibraryStatus,
            addToQueue = addToQueue,
            backgroundColor = backgroundColor
        )
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
    item: AltListItem,
    selected: Boolean,
    thumbnail: Bitmap?,
    playItem: () -> Unit,
    getChildrenOfItem: () -> Unit,
    libraryState: AltLibraryState?,
    toggleLibraryStatus: (String, Boolean) -> Unit,
    addToQueue: (String) -> Unit,
    backgroundColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .height((144 / LocalDensity.current.density).dp),
    ) {
        // only tracks can be queued
        if (item.type == ContentType.Track) SwipeableListItemRowContent(
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
    item: AltListItem,
    selected: Boolean,
    thumbnail: Bitmap?,
    playItem: () -> Unit,
    getChildrenOfItem: () -> Unit,
    libraryState: AltLibraryState?,
    toggleLibraryStatus: (String, Boolean) -> Unit,
    addToQueue: (String) -> Unit,
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
                addToQueue(item.uri)
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
    item: AltListItem,
    selected: Boolean,
    thumbnail: Bitmap?,
    playItem: () -> Unit,
    getChildrenOfItem: () -> Unit,
    libraryState: AltLibraryState?,
    toggleLibraryStatus: (String, Boolean) -> Unit,
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
    libraryState: AltLibraryState,
    modifier: Modifier = Modifier,
    toggleLibraryStatus: (String, Boolean) -> Unit,
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
                        toggleLibraryStatus(libraryState.uri, !libraryState.isAdded)
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
        item = AltListItem(
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
        toggleLibraryStatus = { _, _ -> },
        addToQueue = {},
        backgroundColor = MaterialTheme.colorScheme.background
    )
}

@Preview(showBackground = true)
@Composable
fun ItemsListPreview() {
    val items = mutableListOf<AltListItem>()
    repeat(5) {
        val alter = it % 2 == 0
        val ali = AltListItem(
            uri = if (alter) "" else "a",
            imageUri = "",
            title = if (alter) "Title" else "TitleTitleTitleTitleTitleTitleTitle",
            subtitle = "Subtitle",
            playable = alter,
            hasChildren = alter
        )
        items.add(ali)
    }
    val backgroundColor = MaterialTheme.colorScheme.background
    LazyColumn {
        browserItemsList(
            listItems = AltListItems(items),
            track = null,
            thumbnailMap = emptyMap(),
            libraryState = mapOf("a" to AltLibraryState(uri = "", isAdded = true, canAdd = true)),
            executeCommand = { },
            backgroundColor = backgroundColor,
            lazyListState = LazyListState(),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemsListPreview2() {
    val items = mutableListOf<AltListItem>()
    repeat(5) {
        val alter = it % 2 == 0
        val ali = AltListItem(
            uri = if (alter) "" else "a",
            imageUri = "",
            title = if (alter) "Title" else "TitleTitleTitleTitleTitleTitleTitle",
            subtitle = "Subtitle",
            playable = alter,
            hasChildren = alter
        )
        items.add(ali)
    }
    val backgroundColor = MaterialTheme.colorScheme.background
    LazyColumn {
        browserItemsList(
            listItems = AltListItems(items = items, total = items.size + 10),
            track = null,
            thumbnailMap = emptyMap(),
            libraryState = mapOf("a" to AltLibraryState(uri = "", isAdded = true, canAdd = true)),
            executeCommand = { },
            backgroundColor = backgroundColor,
            lazyListState = LazyListState()
        )
    }
}