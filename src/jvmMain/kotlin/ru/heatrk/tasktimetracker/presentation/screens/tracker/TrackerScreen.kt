package ru.heatrk.tasktimetracker.presentation.screens.tracker

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro.PomodoroTimer
import ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro.PomodoroTimerComponent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro.PomodoroTimerViewState
import ru.heatrk.tasktimetracker.presentation.screens.tracker.timer.TaskTimer
import ru.heatrk.tasktimetracker.presentation.screens.tracker.timer.TaskTimerComponent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.timer.TaskTimerIntent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.timer.TaskTimerViewState
import ru.heatrk.tasktimetracker.presentation.values.dimens.InsetsDimens

@Composable
fun TrackerScreen(
    taskTimerComponent: TaskTimerComponent,
    pomodoroTimerComponent: PomodoroTimerComponent
) {
    val taskTimerViewState by taskTimerComponent.state.collectAsState()
    val pomodoroTimerViewState by pomodoroTimerComponent.state.collectAsState()

    LaunchedEffect(Unit) {
        taskTimerComponent.addStartListener(pomodoroTimerComponent)
        taskTimerComponent.addStopListener(pomodoroTimerComponent)
        pomodoroTimerComponent.addStopListener(taskTimerComponent)
        pomodoroTimerComponent.addStateChangedListener(taskTimerComponent)
    }

    TrackerScreen(
        taskTimerViewState = taskTimerViewState,
        onTaskTimerIntent = taskTimerComponent::onIntent,
        pomodoroTimerViewState = pomodoroTimerViewState
    )
}

@Composable
fun TrackerScreen(
    taskTimerViewState: TaskTimerViewState,
    onTaskTimerIntent: (TaskTimerIntent) -> Unit,
    pomodoroTimerViewState: PomodoroTimerViewState
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(InsetsDimens.Default)
    ) {
        TaskTimer(
            state = taskTimerViewState,
            onIntent = onTaskTimerIntent,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        Spacer(modifier = Modifier.height(InsetsDimens.Default))

        PomodoroTimer(
            state = pomodoroTimerViewState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

@Composable
@Preview
private fun TrackerScreenPreview() {
    TrackerScreen(
        taskTimerViewState = TaskTimerViewState(timePassed = "00:00:00"),
        onTaskTimerIntent = {},
        pomodoroTimerViewState = PomodoroTimerViewState(remainingTime = "00:00")
    )
}