package ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks

import com.arkivanov.decompose.ComponentContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.heatrk.tasktimetracker.domain.models.TrackedTask
import ru.heatrk.tasktimetracker.domain.repositories.TrackedTasksRepository
import ru.heatrk.tasktimetracker.presentation.screens.base.Component
import ru.heatrk.tasktimetracker.presentation.screens.tracker.TaskStopListener
import ru.heatrk.tasktimetracker.presentation.values.strings.strings
import ru.heatrk.tasktimetracker.util.links.TextToLinkTextConverter
import ru.heatrk.tasktimetracker.util.timer_formatter.MillisecondsFormatter
import java.time.format.DateTimeFormatter

class TrackedTasksComponent(
    componentContext: ComponentContext,
    private val trackedTasksRepository: TrackedTasksRepository,
    private val totalTimeFormatter: MillisecondsFormatter,
    private val dateFormatter: DateTimeFormatter,
    private val textToLinkTextConverter: TextToLinkTextConverter
): Component(componentContext), TaskStopListener {

    private val _state = MutableStateFlow<TrackedTasksViewState>(TrackedTasksViewState.Loading)
    val state = _state.asStateFlow()

    init {
        componentScope.launch {
            updateTrackedTasks()
        }
    }

    override fun onStop(task: TrackedTask) {
        componentScope.launch {
            trackedTasksRepository.addTrackedTask(task)
            updateTrackedTasks()
        }
    }

    private suspend fun updateTrackedTasks() {
        val tasks = trackedTasksRepository.getTrackedTasks()

        if (tasks.isNotEmpty()) {
            _state.value = TrackedTasksViewState.Ok(mapTasksToDayItems(tasks))
        } else {
            _state.value = TrackedTasksViewState.Error(strings.trackedTasksIsEmpty)
        }
    }

    private suspend fun mapTasksToDayItems(tasks: List<TrackedTask>) = tasks
        .groupBy { it.startAt.toLocalDate() }
        .map { entry ->
            val dayTotalTime = entry.value.sumOf { task -> task.duration.toMillis() }

            val items = mutableListOf(mutableListOf<TrackedTask>())

            entry.value.forEachIndexed { index, item ->
                if (index == 0 || shouldBeGrouped(entry.value[index - 1], item)) {
                    items.last().add(item)
                } else {
                    items.add(mutableListOf())
                    items.last().add(item)
                }
            }

            TrackedDayItem(
                totalTime = totalTimeFormatter.format(dayTotalTime),
                title = entry.key.format(dateFormatter),
                items = items.map { mapTasksToGroupItems(it) }
            )
        }.toImmutableList()

    private suspend fun mapTasksToGroupItems(tasks: List<TrackedTask>): TrackedTaskItem {
        val entries = tasks.map {
            TrackedTaskItem.Entry(
                title = it.title.ifBlank { "-" },
                duration = totalTimeFormatter.format(it.duration.toMillis()),
                description = textToLinkTextConverter.convert(it.description.ifBlank { "-" })
            )
        }

        return if (tasks.size == 1) {
            entries[0]
        } else if (tasks.size > 1) {
            val groupTotalTime = tasks.sumOf { task -> task.duration.toMillis() }

            TrackedTaskItem.Group(
                title = entries[0].title,
                duration = totalTimeFormatter.format(groupTotalTime),
                description = entries[0].description,
                entries = entries
            )
        } else {
            throw IllegalArgumentException("tasks argument can't be empty list!")
        }
    }

    private fun shouldBeGrouped(first: TrackedTask, second: TrackedTask) =
        first.title == second.title && first.description == second.description

    data class Args(
        val componentContext: ComponentContext,
    )

    companion object {
        fun create(
            args: Args,
            trackedTasksRepository: TrackedTasksRepository,
            totalTimeFormatter: MillisecondsFormatter,
            dateFormatter: DateTimeFormatter,
            textToLinkTextConverter: TextToLinkTextConverter
        ) = TrackedTasksComponent(
            componentContext = args.componentContext,
            trackedTasksRepository = trackedTasksRepository,
            totalTimeFormatter = totalTimeFormatter,
            dateFormatter = dateFormatter,
            textToLinkTextConverter = textToLinkTextConverter
        )
    }
}