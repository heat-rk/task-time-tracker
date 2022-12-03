package ru.heatrk.tasktimetracker.presentation.values.styles

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DefaultButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Colors.Primary),
        enabled = enabled,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = if (enabled) Color.Black else Color.Gray
        )
    }
}

@Composable
fun AlertButton(
    onClick: () -> Unit,
    text: String,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isEnabled) Color.Red else Color(0xFF8c6d6b)
        ),
        enabled = isEnabled,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = if (isEnabled) Color.White else Color.Gray
        )
    }
}