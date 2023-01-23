package ru.heatrk.tasktimetracker.di

import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance
import ru.heatrk.tasktimetracker.presentation.screens.tracker.TrackerComponent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro.PomodoroTimerComponent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.timer.TaskTimerComponent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks.TrackedTasksComponent

val componentsModule = DI.Module("componentsModule") {
    bindFactory<TrackerComponent.Args, TrackerComponent> { args ->
        TrackerComponent.create(
            args = args,
            di = di
        )
    }

    bindFactory<TaskTimerComponent.Args, TaskTimerComponent> { args ->
        TaskTimerComponent.create(
            args = args,
            timerFormatter = hhMmSsTimeFormatterInstance(),
            defaultDispatcher = defaultDispatcherInstance()
        )
    }

    bindFactory<PomodoroTimerComponent.Args, PomodoroTimerComponent> { args ->
        PomodoroTimerComponent.create(
            args = args,
            getPomodoroConfigUseCase = instance(),
            timerFormatter = mmSsTimeFormatterInstance()
        )
    }

    bindFactory<TrackedTasksComponent.Args, TrackedTasksComponent> { args ->
        TrackedTasksComponent.create(
            args = args,
            trackedTasksRepository = instance(),
            taskToListItemEntry = instance(),
            defaultDispatcher = defaultDispatcherInstance()
        )
    }
}