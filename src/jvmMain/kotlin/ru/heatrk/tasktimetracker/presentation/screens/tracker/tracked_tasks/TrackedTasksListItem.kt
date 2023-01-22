package ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.heatrk.tasktimetracker.util.CompositeKey
import ru.heatrk.tasktimetracker.util.links.LinksTextValue

sealed interface TrackedTasksListItem {
    val id: CompositeKey
    val title: String
    val description: ImmutableList<LinksTextValue>
    val duration: String

    data class Entry(
        override val id: CompositeKey,
        override val title: String,
        override val description: ImmutableList<LinksTextValue>,
        override val duration: String
    ): TrackedTasksListItem

    data class Group(
        override val id: CompositeKey,
        override val title: String,
        override val description: ImmutableList<LinksTextValue>,
        override val duration: String,
        val entries: ImmutableList<Entry>
    ): TrackedTasksListItem
}

data class TrackedDayItem(
    val totalTime: String,
    val title: String,
    val items: ImmutableList<TrackedTasksListItem> = persistentListOf()
)

val TrackedTasksListItem.contentType get() =
    when (this) {
        is TrackedTasksListItem.Entry -> "entry_content_type"
        is TrackedTasksListItem.Group -> "group_content_type"
    }

