package bilal.altify.presentation.screens

import androidx.compose.foundation.layout.*
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

@Composable
fun ErrorScreen(
    icon: Int = R.drawable.error,
    message: String,
    buttonText: String? = null,
    buttonFunc: () -> Unit = {},
    buttonFrontIcon: Int? = null,
    buttonBackIcon: Int? = null,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
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

@Preview
@Composable
private fun Preview() {
    ErrorScreen(
        message = "error here",
        buttonText = "tap",
        buttonFrontIcon = R.drawable.error
    )
}