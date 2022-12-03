package ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.heatrk.tasktimetracker.presentation.values.dimens.InsetsDimens
import ru.heatrk.tasktimetracker.presentation.values.dimens.RadiusDimens
import ru.heatrk.tasktimetracker.presentation.values.dimens.TextSizeDimens
import ru.heatrk.tasktimetracker.presentation.values.styles.Colors

@Composable
fun PomodoroTimer(
    state: PomodoroTimerViewState,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (state.isChilling) {
        Colors.PomodoroChilling
    } else {
        Colors.PomodoroWorking
    }

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(RadiusDimens.Default)
            )
    ) {
        Text(
            text = state.remainingTime,
            color = Colors.PomodoroText,
            fontSize = TextSizeDimens.PomodoroTimer,
            modifier = Modifier.align(Alignment.Center)
                .padding(InsetsDimens.ExtraLarge)
        )
    }
}

@Composable
@Preview
fun PomodoroTimerPreview() {
    PomodoroTimer(
        state = PomodoroTimerViewState(
            remainingTime = "12:53"
        ),
        modifier = Modifier.fillMaxWidth()
    )
}