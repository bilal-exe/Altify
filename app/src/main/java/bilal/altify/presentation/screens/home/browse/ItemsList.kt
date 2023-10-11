package bilal.altify.presentation.screens.home.browse

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
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
import bilal.altify.domain.spotify.use_case.Command
import bilal.altify.domain.spotify.use_case.ContentCommand
import bilal.altify.domain.spotify.use_case.ImagesCommand
import bilal.altify.domain.spotify.use_case.PlaybackCommand
import bilal.altify.domain.spotify.use_case.UserCommand
import bilal.altify.presentation.screens.home.BROWSER_FAB_HEIGHT
import bilal.altify.presentation.util.UpdateEffect
import bilal.altify.presentation.util.clipLen
import bilal.altify.presentation.util.shakeShrinkAnimation
import com.spotify.protocol.types.Image.Dimension
import kotlin.math.ceil


@Composable
fun ItemsList(
    listItems: AltListItems,
    track: String?,
    thumbnailMap: Map<String, Bitmap>,
    libraryState: Map<String, AltLibraryState>,
    executeCommand: (Command) -> Unit,
    browserBackgroundColor: Color,
    lazyListState: LazyListState
) {

    BackHandler {
        executeCommand(ContentCommand.GetPrevious)
    }

    val getRecommended: () -> Unit = {
        executeCommand(ContentCommand.GetRecommended)
    }
    val playItem: (AltListItem) -> Unit = {
        executeCommand(ContentCommand.Play(it))
    }
    val getChildrenOfItem: (AltListItem) -> Unit = {
        executeCommand(ContentCommand.GetChildrenOfItem(it))
    }
    val getThumbnail: (String) -> Unit = {
        executeCommand(ImagesCommand.GetThumbnail(it))
    }
    val addToLibrary: (String) -> Unit = {
        executeCommand(UserCommand.AddToLibrary(it))
    }
    val removeFromLibrary: (String) -> Unit = {
        executeCommand(UserCommand.RemoveFromLibrary(it))
    }
    val addToQueue: (String) -> Unit = {
        executeCommand(PlaybackCommand.AddToQueue(it))
    }

//    var newListLoading by remember { mutableStateOf(false) }
//    LaunchedEffect(key1 = listItems) {
//        newListLoading = false
//    }
//
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
            Log.d("visibleItems", "called")
            Log.d("visibleItems", "${listItems.items.size} ${listItems.total}")
            executeCommand(ContentCommand.LoadMoreChildrenOfItem(listItems))
        }
    }


    val showLoadingIndicator = remember(listItems.items) {
        listItems.items.size < listItems.total
    }

    // progress indicator for when there more list to load
    val progressIndicatorHeight = LocalConfiguration.current.screenHeightDp / 4
//    calculates the height of the list by the thumbnail height plus padding for each list item
//    allows this lazy column to sit in a scrollable without causing an IllegalStateException for nesting

    val density = LocalDensity.current.density
    val columnHeight = remember(listItems.items, density, showLoadingIndicator) {
        (listItems().size * ceil((144 / density) + 16)) + // each listItem is 144 px, with 16 dp of padding
                16 + // extra room
                BROWSER_FAB_HEIGHT +// so floating action button doesn't overlap
                if (showLoadingIndicator) progressIndicatorHeight else 0 // make space for loading animation
    }

    Column {
        GetRecommendedButton(getRecommended)
        LazyColumn(
            modifier = Modifier.height(columnHeight.dp),
        ) {
            items(
                items = listItems(),
                key = { it.id }
            ) { item ->
                ListItemRow(
                    item = item,
                    selected = track == item.uri,
                    thumbnail = thumbnailMap[item.imageUri],
                    playItem = { playItem(item) },
                    getChildrenOfItem = { getChildrenOfItem(item) },
                    libraryState = libraryState[item.uri],
                    addToLibrary = addToLibrary,
                    removeFromLibrary = removeFromLibrary,
                    addToQueue = addToQueue,
                    backgroundColor = browserBackgroundColor
                )
            }
            if (showLoadingIndicator) item {
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

@OptIn(ExperimentalMaterial3Api::class)
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
    addToQueue: (String) -> Unit,
    backgroundColor: Color,
) {
    val rowHeight = with(LocalDensity.current) { Dimension.THUMBNAIL.value.toDp() }
    val thumbnailModifier = Modifier
        .clip(RoundedCornerShape(6.dp))
        .width(rowHeight)
        .aspectRatio(1f)

    val view = LocalView.current
    val haptic = LocalHapticFeedback.current
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToEnd) {
                addToQueue(item.uri)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                    view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                else
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            it != DismissValue.DismissedToEnd
        }
    )

    SwipeToDismiss(
        state = dismissState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .height((144 / LocalDensity.current.density).dp),
        directions = setOf(DismissDirection.StartToEnd),
        background = {
            dismissState.dismissDirection ?: return@SwipeToDismiss
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f, label = ""
            )
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
            Row(
                modifier = Modifier
                    .background(
                        color = if (selected) backgroundColor.copy(alpha = 0.25f) else backgroundColor,
                        shape = RoundedCornerShape(8.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
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
                        .clickable { getChildrenOfItem() }
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    ListItemInfo(
                        title = item.title, subtitle = item.subtitle, modifier = Modifier
                    )
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
        },
    )
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
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        if (libraryState.canAdd) {
            val scale = remember { Animatable(1f) }
            val rotation = remember { Animatable(1f) }
            val coroutineScope = rememberCoroutineScope()

            UpdateEffect(libraryState.isAdded) {
                shakeShrinkAnimation(scale = scale, rotation = rotation, scope = coroutineScope)
            }

            Icon(imageVector = icon, contentDescription = "", modifier = Modifier
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
    playItem: () -> Unit, modifier: Modifier = Modifier, playable: Boolean
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
        addToLibrary = {},
        removeFromLibrary = {},
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
    ItemsList(
        listItems = AltListItems(items),
        track = null,
        thumbnailMap = emptyMap(),
        libraryState = mapOf("a" to AltLibraryState(uri = "", isAdded = true, canAdd = true)),
        executeCommand = { },
        browserBackgroundColor = MaterialTheme.colorScheme.background,
        lazyListState = LazyListState(),
    )
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
    ItemsList(
        listItems = AltListItems(items = items, total = items.size + 10),
        track = null,
        thumbnailMap = emptyMap(),
        libraryState = mapOf("a" to AltLibraryState(uri = "", isAdded = true, canAdd = true)),
        executeCommand = { },
        browserBackgroundColor = MaterialTheme.colorScheme.background,
        lazyListState = LazyListState()
    )
}