package bilal.altify.presentation.screens.nowplaying

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import bilal.altify.data.dataclasses.AltPlayerContext
import bilal.altify.data.util.clipLen
import bilal.altify.presentation.util.AltText

@Composable
fun NowPlayingTopBar(
    player: AltPlayerContext,
    modifier: Modifier = Modifier,
    rightButtonIcon: ImageVector? = null,
    rightButtonIconContentDescription: String = "",
    onRightButtonClick: (() -> Unit)? = null,
) {
    val title = player.title
    val subtitle = player.subtitle
    val type = player.type
    NowPlayingTopBar(
        title = title,
        subtitle = subtitle,
        type = type,
        modifier = modifier,
        actionIcon = rightButtonIcon,
        actionIconContentDescription = rightButtonIconContentDescription,
        onActionClick = onRightButtonClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NowPlayingTopBar(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    type: String,
    actionIcon: ImageVector? = null,
    actionIconContentDescription: String = "",
    onActionClick: (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        title = { TopAppBarText(title, subtitle, type) },
        actions = {
            if (actionIcon != null && onActionClick != null)
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = actionIconContentDescription,
                        tint = titleColor,
                    )
                }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@Composable
private fun TopAppBarText(
    title: String,
    subtitle: String,
    type: String,
) {
    AltText(
        buildAnnotatedString {
            withStyle(SpanStyle()) { append("$subtitle: ") }
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                append(title.clipLen(20))
            }
        },
        isTitle = true
    )
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingTopBarPreview() {
    NowPlayingBackground {
        NowPlayingTopBar(
            title = "Title",
            subtitle = "Subtitle",
            type = "Type",
            actionIcon = Icons.Default.Settings,
            onActionClick = {}
        )
    }
}