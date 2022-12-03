package ru.heatrk.tasktimetracker.presentation.screens.tracker.timer

data class TaskTimerViewState(
    val timePassed: String,
    val taskName: String = "",
    val taskUrl: String = "",
    val isRunning: Boolean = false,
    val isEnabled: Boolean = true
)

sealed class TaskTimerIntent {
    class OnTaskNameChange(val value: String): TaskTimerIntent()
    class OnTaskUrlChange(val value: String): TaskTimerIntent()
    object OnStartButtonClick: TaskTimerIntent()
    object OnStopButtonClick: TaskTimerIntent()
}