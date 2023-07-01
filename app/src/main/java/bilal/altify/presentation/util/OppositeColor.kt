package bilal.altify.presentation.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun getComplementaryColor(color: Color): Color =
    Color(color.toArgb() xor 0x00ffffff)