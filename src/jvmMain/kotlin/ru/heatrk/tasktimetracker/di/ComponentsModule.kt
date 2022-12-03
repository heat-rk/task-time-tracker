package ru.heatrk.tasktimetracker.di

import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance
import ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro.PomodoroTimerComponent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.timer.TaskTimerComponent

val componentsModule = DI.Module("componentsModule") {
    bindFactory<TaskTimerComponent.Args, TaskTimerComponent> { args ->
        TaskTimerComponent.create(
            args = args,
            timerFormatter = stopWatchTimerFormatterInstance()
        )
    }

    bindFactory<PomodoroTimerComponent.Args, PomodoroTimerComponent> { args ->
        PomodoroTimerComponent.create(
            args = args,
            getPomodoroConfigUseCase = instance(),
            timerFormatter = minutesCountdownTimerFormatterInstance()
        )
    }
}