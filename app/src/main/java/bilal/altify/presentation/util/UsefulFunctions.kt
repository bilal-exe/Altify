package bilal.altify.presentation.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.core.app.ActivityCompat
import androidx.palette.graphics.Palette
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.math.max
import kotlin.math.min

fun Context.quickCheckPerms(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Palette.Swatch.getColor() = Color(rgb)

@Composable
fun SetStatusBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(key1 = color) {
        systemUiController.setStatusBarColor(color = color)
    }
}

fun Color.complement(): Color {
    val rgb = this.convert(ColorSpaces.Srgb)
    val r = rgb.red
    val g = rgb.green
    val b = rgb.blue

    val sumOfHighestLowest = max(max(r, g), b) + min(r, min(g, b))

    return Color(
        red = sumOfHighestLowest - r,
        blue = sumOfHighestLowest - b,
        green = sumOfHighestLowest - g,
        alpha = this.alpha
    )
}