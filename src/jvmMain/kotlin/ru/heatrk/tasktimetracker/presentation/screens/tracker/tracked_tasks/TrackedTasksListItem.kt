package ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks

import kotlinx.collections.immutable.ImmutableList
import ru.heatrk.tasktimetracker.util.links.LinksTextValue
import java.time.LocalDate

sealed interface TrackedTasksListItem {
    val key: String
    val title: String
    val duration: String
    val description: ImmutableList<LinksTextValue>
    val localDate: LocalDate
    val contentType: String

    data class Entry(
        override val key: String,
        override val title: String,
        override val duration: String,
        override val description: ImmutableList<LinksTextValue>,
        override val localDate: LocalDate
    ): TrackedTasksListItem {
        override val contentType = ENTRY_CONTENT_TYPE
    }

    data class Group(
        override val title: String,
        override val duration: String,
        override val description: ImmutableList<LinksTextValue>,
        override val localDate: LocalDate,
        val isEntriesShown: Boolean = false,
        val entries: ImmutableList<Entry>
    ): TrackedTasksListItem {
        override val key = localDate.toString() + title + description.toString()
        override val contentType = GROUP_CONTENT_TYPE
    }

    companion object {
        private const val ENTRY_CONTENT_TYPE = "entry_content_type"
        private const val GROUP_CONTENT_TYPE = "group_content_type"
    }
}

data class TrackedDayItem(
    val totalTime: String,
    val title: String,
    val items: ImmutableList<TrackedTasksListItem>
)