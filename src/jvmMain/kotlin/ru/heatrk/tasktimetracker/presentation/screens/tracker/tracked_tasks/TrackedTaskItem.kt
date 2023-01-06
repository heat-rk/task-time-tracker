package ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks

import kotlinx.collections.immutable.ImmutableList
import ru.heatrk.tasktimetracker.util.links.LinksTextValue

sealed interface TrackedTaskItem {
    val title: String
    val duration: String
    val description: ImmutableList<LinksTextValue>

    data class Entry(
        override val title: String,
        override val duration: String,
        override val description: ImmutableList<LinksTextValue>
    ): TrackedTaskItem

    data class Group(
        override val title: String,
        override val duration: String,
        override val description: ImmutableList<LinksTextValue>,
        val isEntriesShown: Boolean = false,
        val entries: List<Entry>
    ): TrackedTaskItem
}

data class TrackedDayItem(
    val totalTime: String,
    val title: String,
    val items: List<TrackedTaskItem>
)