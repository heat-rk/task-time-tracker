package ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.heatrk.tasktimetracker.presentation.values.dimens.InsetsDimens
import ru.heatrk.tasktimetracker.presentation.values.dimens.TextSizeDimens
import ru.heatrk.tasktimetracker.presentation.values.styles.ApplicationTheme

@Composable
fun PomodoroTimer(
    state: PomodoroTimerViewState,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (state.isChilling) {
        ApplicationTheme.colors.pomodoroChilling
    } else {
        ApplicationTheme.colors.pomodoroWorking
    }

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = ApplicationTheme.shapes.medium
            )
    ) {
        Text(
            text = state.remainingTime,
            color = ApplicationTheme.colors.onPomodoro,
            fontSize = TextSizeDimens.PomodoroTimer,
            modifier = Modifier.align(Alignment.Center)
                .padding(InsetsDimens.ExtraLarge)
        )
    }
}

@Composable
@Preview
fun PomodoroTimerPreview() {
    ApplicationTheme {
        PomodoroTimer(
            state = PomodoroTimerViewState(
                remainingTime = "12:53"
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}