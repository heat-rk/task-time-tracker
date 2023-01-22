package ru.heatrk.tasktimetracker.mappers

import kotlinx.collections.immutable.toImmutableList
import ru.heatrk.tasktimetracker.domain.models.TrackedTask
import ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks.TrackedDayItem
import ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks.TrackedTasksListItem
import ru.heatrk.tasktimetracker.util.CompositeKey
import ru.heatrk.tasktimetracker.util.links.TextToLinkTextConverter
import ru.heatrk.tasktimetracker.util.time_formatter.local_date.LocalDateFormatter
import ru.heatrk.tasktimetracker.util.time_formatter.millis.MillisecondsFormatter
import java.time.LocalDate

class TrackedTaskDomainToListItemsMapper(
    private val totalTimeFormatter: MillisecondsFormatter,
    private val textToLinkTextConverter: TextToLinkTextConverter,
    private val dateFormatter: LocalDateFormatter
) {
    suspend fun entryOf(task: TrackedTask) = TrackedTasksListItem.Entry(
        id = CompositeKey(task.id),
        title = task.title.ifBlank { "-" },
        duration = totalTimeFormatter.format(task.duration.toMillis()),
        description = textToLinkTextConverter.convert(task.description.ifBlank { "-" })
    )

    suspend fun groupOf(
        date: LocalDate,
        tasks: List<TrackedTask>
    ): TrackedTasksListItem.Group {
        val entries = tasks.map { entryOf(it) }

        val groupTotalTime = tasks.sumOf { task -> task.duration.toMillis() }

        val title = entries[0].title
        val description = entries[0].description

        return TrackedTasksListItem.Group(
            id = CompositeKey(date, title, description),
            title = title,
            duration = totalTimeFormatter.format(groupTotalTime),
            description = description,
            entries = entries.toImmutableList()
        )
    }

    suspend fun dayOf(
        date: LocalDate,
        tasks: List<TrackedTask>
    ): TrackedDayItem {
        if (tasks.isEmpty()) {
            throw IllegalArgumentException("Tasks argument of day can't be empty list!")
        }

        val dayTotalTime = tasks.sumOf { task -> task.duration.toMillis() }

        return TrackedDayItem(
            totalTime = totalTimeFormatter.format(dayTotalTime),
            title = dateFormatter.format(date),
            items = tasks
                .groupBy { it.title to it.description }
                .map {
                    if (it.value.isEmpty()) {
                        throw IllegalArgumentException("Internal grouping failed!")
                    } else if (it.value.size == 1) {
                        entryOf(it.value[0])
                    } else {
                        groupOf(date = date, tasks = it.value)
                    }
                }.toImmutableList()
        )
    }

    suspend fun daysOf(tasks: List<TrackedTask>) =
        tasks
            .groupBy { it.startAt.toLocalDate() }
            .map {
                dayOf(date = it.key, tasks = it.value)
            }
}