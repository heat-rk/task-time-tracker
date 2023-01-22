package ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks

import com.arkivanov.decompose.ComponentContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.heatrk.tasktimetracker.domain.models.TrackedTask
import ru.heatrk.tasktimetracker.domain.repositories.TrackedTasksRepository
import ru.heatrk.tasktimetracker.mappers.TrackedTaskDomainToListItemsMapper
import ru.heatrk.tasktimetracker.presentation.screens.base.Component
import ru.heatrk.tasktimetracker.presentation.screens.tracker.TaskStopListener
import ru.heatrk.tasktimetracker.presentation.values.strings.strings
import ru.heatrk.tasktimetracker.util.requireValueOfType
import ru.heatrk.tasktimetracker.util.requireValueOfTypeOrNull

class TrackedTasksComponent(
    componentContext: ComponentContext,
    private val defaultDispatcher: CoroutineDispatcher,
    private val trackedTasksRepository: TrackedTasksRepository,
    private val taskToListItemEntry: TrackedTaskDomainToListItemsMapper
): Component(componentContext), TaskStopListener {

    private val _state = MutableStateFlow<TrackedTasksViewState>(TrackedTasksViewState.Loading)
    val state = _state.asStateFlow()

    init {
        componentScope.launch {
            val tasks = trackedTasksRepository.getTrackedTasks()

            if (tasks.isNotEmpty()) {
                _state.value = TrackedTasksViewState.Ok(
                    taskToListItemEntry.daysOf(tasks).toImmutableList()
                )
            } else {
                _state.value = TrackedTasksViewState.Error(strings.trackedTasksIsEmpty)
            }
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

            val state = state.requireValueOfTypeOrNull<TrackedTasksViewState.Ok>()

            if (state != null) {
                val tasks = trackedTasksRepository.getTrackedTasks()

                if (tasks.isNotEmpty()) {
                    _state.value = state.copy(
                        items = taskToListItemEntry.daysOf(tasks).toImmutableList()
                    )
                } else {
                    _state.value = TrackedTasksViewState.Error(strings.trackedTasksIsEmpty)
                }
            }
        }
    }

    private suspend fun onItemClick(item: TrackedTasksListItem) = withContext(defaultDispatcher) {
        when (item) {
            is TrackedTasksListItem.Entry -> {

            }

            is TrackedTasksListItem.Group -> {
                val state = _state.requireValueOfType<TrackedTasksViewState.Ok>()
                val entriesShownFor = state.entriesShownFor.toMutableList()

                if (state.entriesShownFor.contains(item.id)) {
                    entriesShownFor.remove(item.id)
                } else {
                    entriesShownFor.add(item.id)
                }

                _state.value = state.copy(
                    entriesShownFor = entriesShownFor.toImmutableSet()
                )
            }
        }
    }

    data class Args(
        val componentContext: ComponentContext,
    )

    companion object {
        fun create(
            args: Args,
            trackedTasksRepository: TrackedTasksRepository,
            taskToListItemEntry: TrackedTaskDomainToListItemsMapper,
            defaultDispatcher: CoroutineDispatcher
        ) = TrackedTasksComponent(
            componentContext = args.componentContext,
            trackedTasksRepository = trackedTasksRepository,
            taskToListItemEntry = taskToListItemEntry,
            defaultDispatcher = defaultDispatcher
        )
    }
}