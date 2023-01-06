package ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks

import kotlinx.collections.immutable.ImmutableList

sealed interface TrackedTasksViewState {
    object Loading: TrackedTasksViewState
    data class Error(val message: String): TrackedTasksViewState
    data class Ok(
        val items: ImmutableList<TrackedDayItem>
    ): TrackedTasksViewState
}

sealed interface TrackedTasksIntent {
    data class OnItemClick(val item: TrackedTasksListItem): TrackedTasksIntent
}