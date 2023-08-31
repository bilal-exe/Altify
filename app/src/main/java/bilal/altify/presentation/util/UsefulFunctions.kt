package bilal.altify.presentation.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.palette.graphics.Palette
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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