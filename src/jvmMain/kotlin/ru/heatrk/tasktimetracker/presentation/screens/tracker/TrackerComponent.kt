package ru.heatrk.tasktimetracker.presentation.screens.tracker

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.kodein.di.DI
import org.kodein.di.instance
import ru.heatrk.tasktimetracker.presentation.screens.base.Component
import ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro.PomodoroTimerComponent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro.PomodoroTimerIntent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.timer.TaskTimerComponent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.timer.TaskTimerIntent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks.TrackedTasksComponent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks.TrackedTasksIntent

class TrackerComponent(
    componentContext: ComponentContext,
    di: DI
): Component(componentContext) {
    val trackedTasksComponent by di.instance<TrackedTasksComponent.Args, TrackedTasksComponent>(
        arg = TrackedTasksComponent.Args(componentContext = childContext("tasks"))
    )

    val pomodoroTimerComponent by di.instance<PomodoroTimerComponent.Args, PomodoroTimerComponent>(
        arg = PomodoroTimerComponent.Args(componentContext = childContext("pomodoro"))
    )

    val taskTimerComponent by di.instance<TaskTimerComponent.Args, TaskTimerComponent>(
        arg = TaskTimerComponent.Args(componentContext = childContext("timer"))
    )

    init {
        taskTimerComponent.timerStartEvents
            .onEach {
                pomodoroTimerComponent.onIntent(PomodoroTimerIntent.OnTimerStart)
            }
            .launchIn(componentScope)

        taskTimerComponent.timerStopEvents
            .onEach {
                pomodoroTimerComponent.onIntent(PomodoroTimerIntent.OnTimerStop)
            }
            .launchIn(componentScope)

        taskTimerComponent.timerRegisterTaskEvents
            .onEach { task ->
                trackedTasksComponent.onIntent(TrackedTasksIntent.OnTaskAdded(task))
            }
            .launchIn(componentScope)

        pomodoroTimerComponent.pomodoroWorkingFinishedEvents
            .onEach {
                taskTimerComponent.onIntent(TaskTimerIntent.OnPomodoroWorkingFinished)
            }
            .launchIn(componentScope)

        pomodoroTimerComponent.pomodoroStateChangedEvents
            .onEach { state ->
                taskTimerComponent.onIntent(TaskTimerIntent.OnPomodoroStateChanged(state))
            }
            .launchIn(componentScope)
    }

    data class Args(
        val componentContext: ComponentContext
    )

    companion object {
        fun create(
            args: Args,
            di: DI
        ) = TrackerComponent(
            componentContext = args.componentContext,
            di = di
        )
    }
}