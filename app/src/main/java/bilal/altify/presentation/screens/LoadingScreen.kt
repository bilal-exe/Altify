package bilal.altify.presentation.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bilal.altify.R
import bilal.altify.presentation.theme.AltifyTheme

@Composable
fun LoadingScreen(
    text: String = "Loading...",
    darkTheme: Boolean = isSystemInDarkTheme(),
    modifier: Modifier = Modifier
) {
    // todo extract this for use with other screens
    val infiniteTransition = rememberInfiniteTransition(label = "Loading Animation")
    val bounce by infiniteTransition.animateFloat(
        initialValue = -150f,
        targetValue = 150f,
        animationSpec = infiniteRepeatable(
            tween(1000, 0, FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "Loading Bounce Animation"
    )
    val spin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(1000, 0, FastOutLinearInEasing)
        ),
        label = "Loading Rotate Animation"
    )
    AltifyTheme (
        darkTheme = darkTheme
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                painter = painterResource(id = R.drawable.refresh),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer {
                        translationY = bounce
                        rotationZ = spin
                    }
            )
            Text(
                text = text,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AltifyTheme { LoadingScreen() }
}