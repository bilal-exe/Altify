package bilal.altify.presentation.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.annotation.RequiresApi
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.core.app.ActivityCompat
import androidx.palette.graphics.Palette
import bilal.altify.presentation.DarkThemeConfig
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
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

@Composable
fun shouldUseDarkTheme(
    darkThemeConfig: DarkThemeConfig?
): Boolean =
    when (darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
        null -> isSystemInDarkTheme()
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
    val spec = tween<Float>(durationMillis = 100)
    scope.launch {
        scale.animateTo(1.4f)
        delay(500)
        scale.animateTo(0.6f)
        scale.animateTo(1.4f)
        scale.animateTo(1f)
    }
    scope.launch {
        rotation.animateTo(35f, spec)
        rotation.animateTo(-35f, spec)
        rotation.animateTo(0f, spec)
    }
}

@Composable
fun ShakeBounceAnimation(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val contentScale = remember { Animatable(1f) }
    val contentRotation = remember { Animatable(1f) }
    UpdateEffect(key = icon) {
        shakeShrinkAnimation(contentScale, contentRotation, scope)
    }
    Box(
        modifier = modifier
            .scale(contentScale.value)
            .rotate(contentRotation.value)
    ) { content() }
}

//@Preview
//@Composable
//fun BouncePopAnimationPreview() {
//    BackgroundCircleAnimation(
//        modifier = Modifier
//            .aspectRatio(1f)
//            .fillMaxWidth()
//    ) {
//        Icon(imageVector = Icons.Default.Favorite, contentDescription = "")
//    }
//}