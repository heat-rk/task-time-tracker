package ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro

sealed interface PomodoroTimerIntent {
    object OnTimerStart: PomodoroTimerIntent
    object OnTimerStop: PomodoroTimerIntent
}

data class PomodoroTimerViewState(
    val remainingTime: String,
    val isChilling: Boolean = false
)