package ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro

import ru.heatrk.tasktimetracker.domain.models.PomodoroState

interface PomodoroStateChangedListener {
    fun onStateChanged(state: PomodoroState)
}