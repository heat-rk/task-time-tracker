package ru.heatrk.tasktimetracker.presentation.screens.tracker

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.persistentListOf
import ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro.PomodoroTimer
import ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro.PomodoroTimerComponent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro.PomodoroTimerViewState
import ru.heatrk.tasktimetracker.presentation.screens.tracker.timer.TaskTimer
import ru.heatrk.tasktimetracker.presentation.screens.tracker.timer.TaskTimerComponent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.timer.TaskTimerIntent
import ru.heatrk.tasktimetracker.presentation.screens.tracker.timer.TaskTimerViewState
import ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks.*
import ru.heatrk.tasktimetracker.presentation.values.dimens.InsetsDimens
import ru.heatrk.tasktimetracker.presentation.values.styles.ApplicationTheme
import ru.heatrk.tasktimetracker.util.links.LinksTextValue
import java.time.LocalDate

@Composable
fun TrackerScreen(
    taskTimerComponent: TaskTimerComponent,
    pomodoroTimerComponent: PomodoroTimerComponent,
    trackedTasksComponent: TrackedTasksComponent
) {
    val taskTimerViewState by taskTimerComponent.state.collectAsState()
    val pomodoroTimerViewState by pomodoroTimerComponent.state.collectAsState()
    val trackedTasksViewState by trackedTasksComponent.state.collectAsState()

    LaunchedEffect(Unit) {
        taskTimerComponent.addStartListener(pomodoroTimerComponent)
        taskTimerComponent.addStopListener(pomodoroTimerComponent)
        taskTimerComponent.addTaskStopListener(trackedTasksComponent)
        pomodoroTimerComponent.addStopListener(taskTimerComponent)
        pomodoroTimerComponent.addStateChangedListener(taskTimerComponent)
    }

    TrackerScreen(
        taskTimerViewState = taskTimerViewState,
        onTaskTimerIntent = taskTimerComponent::onIntent,
        pomodoroTimerViewState = pomodoroTimerViewState,
        trackedTasksViewState = trackedTasksViewState,
        onTrackedTasksIntent = trackedTasksComponent::onIntent
    )
}

@Composable
fun TrackerScreen(
    taskTimerViewState: TaskTimerViewState,
    onTaskTimerIntent: (TaskTimerIntent) -> Unit,
    pomodoroTimerViewState: PomodoroTimerViewState,
    trackedTasksViewState: TrackedTasksViewState,
    onTrackedTasksIntent: (TrackedTasksIntent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TaskTimer(
            state = taskTimerViewState,
            onIntent = onTaskTimerIntent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = InsetsDimens.Default)
                .padding(top = InsetsDimens.Default)
        )

        Spacer(modifier = Modifier.height(InsetsDimens.Default))

        PomodoroTimer(
            state = pomodoroTimerViewState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = InsetsDimens.Default)
        )

        TrackedTasks(
            state = trackedTasksViewState,
            onIntent = onTrackedTasksIntent,
            contentPadding = PaddingValues(all = InsetsDimens.Default),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
@Preview
private fun TrackerScreenPreview() {
    fun link(url: String) = persistentListOf(
        LinksTextValue.Link(
            text = url,
            tag = url,
            annotation = url
        )
    )

    val days = persistentListOf(
        TrackedDayItem(
            totalTime = "00:28:36",
            title = "??????????????",
            items = persistentListOf(
                TrackedTasksListItem.Entry(
                    key = "1",
                    localDate = LocalDate.now(),
                    title = "MOBPF-128",
                    description = link("https://jira.com/MOBPF-128"),
                    duration = "00:12:36"
                ),
                TrackedTasksListItem.Group(
                    title = "MOBPF-130",
                    localDate = LocalDate.now(),
                    description = link("https://jira.com/MOBPF-130"),
                    duration = "00:16:00",
                    isEntriesShown = true,
                    entries = persistentListOf(
                        TrackedTasksListItem.Entry(
                            key = "2",
                            localDate = LocalDate.now(),
                            title = "MOBPF-130",
                            description = link("https://jira.com/MOBPF-130"),
                            duration = "00:09:00"
                        ),
                        TrackedTasksListItem.Entry(
                            key = "3",
                            localDate = LocalDate.now(),
                            title = "MOBPF-130",
                            description = link("https://jira.com/MOBPF-130"),
                            duration = "00:07:00"
                        )
                    )
                )
            )
        ),
        TrackedDayItem(
            totalTime = "00:28:36",
            title = "??????????",
            items = persistentListOf(
                TrackedTasksListItem.Entry(
                    key = "4",
                    localDate = LocalDate.now().minusDays(1),
                    title = "MOBPF-128",
                    description = link("https://jira.com/MOBPF-128"),
                    duration = "00:12:36"
                ),
                TrackedTasksListItem.Group(
                    localDate = LocalDate.now().minusDays(1),
                    title = "MOBPF-130",
                    description = link("https://jira.com/MOBPF-130"),
                    duration = "00:16:00",
                    entries = persistentListOf(
                        TrackedTasksListItem.Entry(
                            key = "5",
                            localDate = LocalDate.now().minusDays(1),
                            title = "MOBPF-130",
                            description = link("https://jira.com/MOBPF-130"),
                            duration = "00:09:00"
                        ),
                        TrackedTasksListItem.Entry(
                            key = "6",
                            localDate = LocalDate.now().minusDays(1),
                            title = "MOBPF-130",
                            description = link("https://jira.com/MOBPF-130"),
                            duration = "00:07:00"
                        )
                    )
                )
            )
        )
    )

    ApplicationTheme {
        TrackerScreen(
            taskTimerViewState = TaskTimerViewState(timePassed = "00:00:00"),
            onTaskTimerIntent = {},
            pomodoroTimerViewState = PomodoroTimerViewState(remainingTime = "00:00"),
            trackedTasksViewState = TrackedTasksViewState.Ok(days),
            onTrackedTasksIntent = {}
        )
    }
}