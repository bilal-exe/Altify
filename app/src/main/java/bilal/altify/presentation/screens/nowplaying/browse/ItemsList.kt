package bilal.altify.presentation.screens.nowplaying.browse

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.model.AltTrack


@Composable
fun ItemsList(
    listItems: List<AltListItem>,
    track: AltTrack?,
    palette: Palette?
) {
    LazyColumn {
        items(
            items = listItems,
            key = { it.id },
        ) {
            ListItemRow(it, track?.uri == it.uri)
        }
    }
}

@Composable
fun ListItemRow(
    item: AltListItem,
    selected: Boolean
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {

    }
}
