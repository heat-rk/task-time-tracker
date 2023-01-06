package ru.heatrk.tasktimetracker.presentation.screens.tracker

import ru.heatrk.tasktimetracker.domain.models.TrackedTask

interface TimerStartListener {
    fun onStart()
}

interface TimerStopListener {
    fun onStop()
}

interface TaskStopListener {
    fun onStop(task: TrackedTask)
}