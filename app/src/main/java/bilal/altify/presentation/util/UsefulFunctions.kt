package bilal.altify.presentation.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.core.app.ActivityCompat
import androidx.palette.graphics.Palette
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

fun Context.quickCheckPerms(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun <T> Flow<T>.collectLatestOn(scope: CoroutineScope, action: suspend (value: T) -> Unit) {
    scope.launch { this@collectLatestOn.collectLatest(action) }
}

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

/**
 * The same as [LaunchedEffect] but skips the first invocation
 */
@Composable
fun UpdateEffect(key: Any, block: suspend CoroutineScope.() -> Unit) {
    var isTriggered by remember { mutableStateOf(false) }
    LaunchedEffect(key) {
        if (isTriggered) block() else isTriggered = true
    }
}

fun shakeShrinkAnimation(
    scale: Animatable<Float, AnimationVector1D>,
    rotation: Animatable<Float, AnimationVector1D>,
    scope: CoroutineScope
) {
    scope.launch {
        scale.animateTo(0.2f)
        scale.animateTo(1f)
    }
    scope.launch {
        rotation.animateTo(35f)
        rotation.animateTo(-35f)
        rotation.animateTo(35f)
        rotation.animateTo(0f)
    }
}