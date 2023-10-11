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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bilal.altify.R
import bilal.altify.presentation.theme.AltifyTheme

@Composable
fun ErrorScreen(
    icon: Int = R.drawable.error,
    message: String,
    buttonText: String? = null,
    buttonFunc: () -> Unit = {},
    buttonFrontIcon: Int? = null,
    buttonBackIcon: Int? = null,
    darkTheme: Boolean? = null
) {
    AltifyTheme (
        darkTheme = darkTheme?: isSystemInDarkTheme()
    ) {
        Box(
            modifier = Modifier
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
                Icon(
                    painterResource(icon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(250.dp)
                        .padding(20.dp)
                        .fillMaxSize()
                )
                Text(
                    text = message.uppercase(),
                    color = Color.Red,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                if (buttonText != null) {
                    Button(
                        onClick = { buttonFunc() },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp)
                            .size(75.dp)
                    ) {
                        if (buttonFrontIcon != null) painterResource(id = buttonFrontIcon)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = buttonText,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (buttonBackIcon != null) painterResource(id = buttonBackIcon)
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
        buttonFrontIcon = R.drawable.error
    )
}