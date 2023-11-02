package bilal.altify.presentation.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bilal.altify.R
import bilal.altify.presentation.theme.AltifyTheme
import bilal.altify.presentation.util.Icon

data class ErrorScreenInfo(
    val modifier: Modifier = Modifier,
    val icon: Icon = Icon.DrawableResourceIcon(R.drawable.error),
    val message: String,
    val buttonText: String? = null,
    val buttonFunc: () -> Unit = {},
    val buttonFrontIcon: Icon? = null,
    val buttonBackIcon: Icon? = null,
    val darkTheme: Boolean? = null,
)

@Composable
fun ErrorScreen(errorScreen: ErrorScreenInfo) {
    ErrorScreen(
        modifier = errorScreen.modifier,
        icon = errorScreen.icon,
        message = errorScreen.message,
        buttonText = errorScreen.buttonText,
        buttonFunc = errorScreen.buttonFunc,
        buttonFrontIcon = errorScreen.buttonFrontIcon,
        buttonBackIcon = errorScreen.buttonBackIcon,
        darkTheme = errorScreen.darkTheme ?: isSystemInDarkTheme(),
    )
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    icon: Icon = Icon.DrawableResourceIcon(R.drawable.error),
    message: String,
    buttonText: String? = null,
    buttonFunc: () -> Unit = {},
    buttonFrontIcon: Icon? = null,
    buttonBackIcon: Icon? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
) {
    AltifyTheme(
        darkTheme = darkTheme
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(0.8f)
            ) {
                when (icon) {
                    is Icon.DrawableResourceIcon -> Icon(
                        painter = painterResource(icon.id),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(250.dp)
                            .padding(20.dp)
                    )
                    is Icon.ImageVectorIcon -> Icon(
                        imageVector = icon.imageVector,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(250.dp)
                            .padding(20.dp)
                    )
                }
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                if (buttonText != null) {
                    Button(
                        onClick = { buttonFunc() },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp)
                            .size(75.dp)
                    ) {
                        when (buttonFrontIcon) {
                            is Icon.ImageVectorIcon -> Icon(
                                imageVector = buttonFrontIcon.imageVector,
                                contentDescription = null,
                            )
                            is Icon.DrawableResourceIcon -> Icon(
                                painter = painterResource(id = buttonFrontIcon.id),
                                contentDescription = null,
                            )
                            else -> {}
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = buttonText,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        when (buttonBackIcon) {
                            is Icon.ImageVectorIcon -> Icon(
                                imageVector = buttonBackIcon.imageVector,
                                contentDescription = null,
                            )
                            is Icon.DrawableResourceIcon -> Icon(
                                painter = painterResource(id = buttonBackIcon.id),
                                contentDescription = null,
                            )
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ErrorScreen(
        message = "error here",
        buttonText = "tap",
        buttonFrontIcon = Icon.DrawableResourceIcon(R.drawable.error)
    )
}