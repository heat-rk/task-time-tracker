package ru.heatrk.tasktimetracker.presentation.screens.tracker.timer

import ru.heatrk.tasktimetracker.domain.models.PomodoroState

sealed interface TaskTimerIntent {
    data class OnPomodoroStateChanged(val state: PomodoroState): TaskTimerIntent
    data class OnTaskNameChange(val value: String): TaskTimerIntent
    data class OnTaskDescriptionChange(val value: String): TaskTimerIntent
    object OnStartButtonClick: TaskTimerIntent
    object OnStopButtonClick: TaskTimerIntent
    object OnPomodoroWorkingFinished: TaskTimerIntent
}

data class TaskTimerViewState(
    val timePassed: String,
    val taskName: String = "",
    val taskDescription: String = "",
    val isRunning: Boolean = false,
    val isEnabled: Boolean = true
)