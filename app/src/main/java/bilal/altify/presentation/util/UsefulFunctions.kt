package bilal.altify.presentation.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.palette.graphics.Palette

fun Context.quickCheckPerms(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Palette.Swatch.getColor() = Color(rgb)