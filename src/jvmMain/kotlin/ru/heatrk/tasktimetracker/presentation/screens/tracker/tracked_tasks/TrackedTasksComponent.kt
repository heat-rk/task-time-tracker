package ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks

import com.arkivanov.decompose.ComponentContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.heatrk.tasktimetracker.domain.models.TrackedTask
import ru.heatrk.tasktimetracker.domain.repositories.TrackedTasksRepository
import ru.heatrk.tasktimetracker.presentation.screens.base.Component
import ru.heatrk.tasktimetracker.presentation.screens.tracker.TaskStopListener
import ru.heatrk.tasktimetracker.presentation.values.strings.strings
import ru.heatrk.tasktimetracker.util.links.TextToLinkTextConverter
import ru.heatrk.tasktimetracker.util.requireValueOfType
import ru.heatrk.tasktimetracker.util.time_formatter.local_date.LocalDateFormatter
import ru.heatrk.tasktimetracker.util.time_formatter.millis.MillisecondsFormatter

class TrackedTasksComponent(
    componentContext: ComponentContext,
    private val defaultDispatcher: CoroutineDispatcher,
    private val trackedTasksRepository: TrackedTasksRepository,
    private val totalTimeFormatter: MillisecondsFormatter,
    private val dateFormatter: LocalDateFormatter,
    private val textToLinkTextConverter: TextToLinkTextConverter
): Component(componentContext), TaskStopListener {

    private val _state = MutableStateFlow<TrackedTasksViewState>(TrackedTasksViewState.Loading)
    val state = _state.asStateFlow()

    init {
        componentScope.launch {
            updateTrackedTasks()
        }
    }

    fun onIntent(intent: TrackedTasksIntent) = componentScope.launch {
        when (intent) {
            is TrackedTasksIntent.OnItemClick -> {
                onItemClick(intent.item)
            }
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

    private suspend fun onItemClick(item: TrackedTasksListItem) = withContext(defaultDispatcher) {
        when (item) {
            is TrackedTasksListItem.Entry -> {

            }

            is TrackedTasksListItem.Group -> {
                val state = state.requireValueOfType<TrackedTasksViewState.Ok>()

                _state.value = state.copy(
                    items = state.items.map { day ->
                        day.copy(
                            items = day.items.map { other ->
                                if (other.key == item.key) {
                                    item.copy(isEntriesShown = !item.isEntriesShown)
                                } else {
                                    other
                                }
                            }.toImmutableList()
                        )
                    }.toImmutableList()
                )
            }
        }
    }

    private suspend fun mapTasksToDayItems(tasks: List<TrackedTask>) = withContext(defaultDispatcher) {
        tasks
            .groupBy { it.startAt.toLocalDate() }
            .map { entry ->
                val dayTotalTime = entry.value.sumOf { task -> task.duration.toMillis() }

                TrackedDayItem(
                    totalTime = totalTimeFormatter.format(dayTotalTime),
                    title = dateFormatter.format(entry.key),
                    items = entry.value
                        .groupBy { it.title to it.description }
                        .map { mapTasksToGroupItems(it.value) }
                        .toImmutableList()
                )
            }.toImmutableList()
    }

    private suspend fun mapTasksToGroupItems(tasks: List<TrackedTask>): TrackedTasksListItem {
        val entries = tasks.map {
            TrackedTasksListItem.Entry(
                key = it.id.toString(),
                localDate = it.startAt.toLocalDate(),
                title = it.title.ifBlank { "-" },
                duration = totalTimeFormatter.format(it.duration.toMillis()),
                description = textToLinkTextConverter.convert(it.description.ifBlank { "-" })
            )
        }

        return if (tasks.size == 1) {
            entries[0]
        } else if (tasks.size > 1) {
            val groupTotalTime = tasks.sumOf { task -> task.duration.toMillis() }

            TrackedTasksListItem.Group(
                localDate = entries[0].localDate,
                title = entries[0].title,
                duration = totalTimeFormatter.format(groupTotalTime),
                description = entries[0].description,
                entries = entries.toImmutableList()
            )
        } else {
            throw IllegalArgumentException("tasks argument can't be empty list!")
        }
    }

    data class Args(
        val componentContext: ComponentContext,
    )

    companion object {
        fun create(
            args: Args,
            trackedTasksRepository: TrackedTasksRepository,
            totalTimeFormatter: MillisecondsFormatter,
            dateFormatter: LocalDateFormatter,
            textToLinkTextConverter: TextToLinkTextConverter,
            defaultDispatcher: CoroutineDispatcher
        ) = TrackedTasksComponent(
            componentContext = args.componentContext,
            trackedTasksRepository = trackedTasksRepository,
            totalTimeFormatter = totalTimeFormatter,
            dateFormatter = dateFormatter,
            textToLinkTextConverter = textToLinkTextConverter,
            defaultDispatcher = defaultDispatcher
        )
    }
}