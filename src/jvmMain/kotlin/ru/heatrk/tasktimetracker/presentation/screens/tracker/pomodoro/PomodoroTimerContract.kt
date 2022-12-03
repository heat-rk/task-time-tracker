package ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro

data class PomodoroTimerViewState(
    val remainingTime: String,
    val isChilling: Boolean = false
)