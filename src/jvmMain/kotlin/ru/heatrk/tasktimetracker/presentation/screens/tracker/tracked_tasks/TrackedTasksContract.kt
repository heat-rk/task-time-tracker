package ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import ru.heatrk.tasktimetracker.util.CompositeKey

sealed interface TrackedTasksViewState {
    object Loading: TrackedTasksViewState
    data class Error(val message: String): TrackedTasksViewState
    data class Ok(
        val items: ImmutableList<TrackedDayItem>,
        val entriesShownFor: ImmutableSet<CompositeKey> = persistentSetOf()
    ): TrackedTasksViewState
}

sealed interface TrackedTasksIntent {
    data class OnItemClick(val item: TrackedTasksListItem): TrackedTasksIntent
}