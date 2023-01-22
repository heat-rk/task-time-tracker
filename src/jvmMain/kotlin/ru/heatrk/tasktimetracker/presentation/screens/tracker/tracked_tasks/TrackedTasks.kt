package ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import ru.heatrk.tasktimetracker.presentation.values.dimens.InsetsDimens
import ru.heatrk.tasktimetracker.presentation.values.styles.ApplicationTheme
import ru.heatrk.tasktimetracker.util.CompositeKey
import ru.heatrk.tasktimetracker.util.links.LinksTextValue

@Composable
fun TrackedTasks(
    state: TrackedTasksViewState,
    onIntent: (TrackedTasksIntent) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    when (state) {
        is TrackedTasksViewState.Ok -> {
            TrackedTasksOkState(
                state = state,
                onIntent = onIntent,
                contentPadding = contentPadding,
                modifier = modifier
            )
        }

        is TrackedTasksViewState.Error -> {
            TrackedTasksErrorState(
                error = state,
                modifier = modifier
            )
        }

        TrackedTasksViewState.Loading -> {
            TrackedTasksLoadingState(
                modifier = modifier
            )
        }
    }
}

@Composable
private fun TrackedTasksOkState(
    state: TrackedTasksViewState.Ok,
    onIntent: (TrackedTasksIntent) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        state.items.forEachIndexed { dayIndex, day ->
            item(key = day.title) {
                TrackedTasksDayHeader(
                    dateTitle = day.title,
                    totalTime = day.totalTime,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            day.items.forEachIndexed { taskIndex, task ->
                when (task) {
                    is TrackedTasksListItem.Entry -> {
                        entryItem(
                            day = day,
                            item = task,
                            position = taskIndex,
                            onIntent = onIntent
                        )
                    }
                    is TrackedTasksListItem.Group -> {
                        groupItem(
                            day = day,
                            item = task,
                            isEntriesShown = state.entriesShownFor.contains(task.id),
                            position = taskIndex,
                            onIntent = onIntent
                        )
                    }
                }
            }

            if (dayIndex != state.items.lastIndex) {
                item {
                    Spacer(modifier = Modifier.height(InsetsDimens.Default))
                }
            }
        }
    }
}

@Composable
private fun TrackedTasksErrorState(
    error: TrackedTasksViewState.Error,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(InsetsDimens.Default),
        contentAlignment = Alignment.Center
    ) {
        Text(text = error.message)
    }
}

@Composable
private fun TrackedTasksLoadingState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(InsetsDimens.Default),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

private fun LazyListScope.entryItem(
    day: TrackedDayItem,
    item: TrackedTasksListItem.Entry,
    position: Int,
    onIntent: (TrackedTasksIntent) -> Unit
) {
    item(key =  item.id, contentType = item.contentType) {
        TrackedTasksEntry(
            item = item,
            isBottom = isBottom(
                items = day.items,
                position = position
            ),
            onClick = { onIntent(TrackedTasksIntent.OnItemClick(item)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun LazyListScope.groupItem(
    day: TrackedDayItem,
    item: TrackedTasksListItem.Group,
    isEntriesShown: Boolean,
    position: Int,
    onIntent: (TrackedTasksIntent) -> Unit
) {
    item(key =  item.id, contentType = item.contentType) {
        TrackedTasksGroup(
            item = item,
            counterValue = item.entries.size,
            isSelected = isEntriesShown,
            isBottom = if (isEntriesShown) {
                isBottom(
                    items = day.items,
                    position = position
                )
            } else {
               false
            },
            onClick = { onIntent(TrackedTasksIntent.OnItemClick(item)) },
            modifier = Modifier.fillMaxWidth()
        )
    }


    if (isEntriesShown) {
        itemsIndexed(
            items = item.entries,
            key = { _, innerItem ->  innerItem.id },
            contentType = { _, innerItem -> innerItem.contentType }
        ) { innerPosition, innerItem ->
            val isBottom = if (position == day.items.lastIndex) {
                isBottom(
                    items = item.entries,
                    position = innerPosition
                )
            } else {
                false
            }

            TrackedTasksEntry(
                item = innerItem,
                isInner = true,
                isBottom = isBottom,
                onClick = { onIntent(TrackedTasksIntent.OnItemClick(innerItem)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun isBottom(
    items: List<TrackedTasksListItem>,
    position: Int
): Boolean {
    return position == items.lastIndex
}

@Preview
@Composable
private fun TrackedTasksPreview() {
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
            title = "Сегодня",
            items = persistentListOf(
                TrackedTasksListItem.Entry(
                    id = CompositeKey(1),
                    title = "MOBPF-128",
                    description = link("https://jira.com/MOBPF-128"),
                    duration = "00:12:36"
                ),
                TrackedTasksListItem.Group(
                    id = CompositeKey(10),
                    title = "MOBPF-130",
                    description = link("https://jira.com/MOBPF-130"),
                    duration = "00:16:00",
                    entries = persistentListOf(
                        TrackedTasksListItem.Entry(
                            id = CompositeKey(2),
                            title = "MOBPF-130",
                            description = link("https://jira.com/MOBPF-130"),
                            duration = "00:09:00"
                        ),
                        TrackedTasksListItem.Entry(
                            id = CompositeKey(3),
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
            title = "Вчера",
            items = persistentListOf(
                TrackedTasksListItem.Entry(
                    id = CompositeKey(4),
                    title = "MOBPF-128",
                    description = link("https://jira.com/MOBPF-128"),
                    duration = "00:12:36"
                ),
                TrackedTasksListItem.Group(
                    id = CompositeKey(11),
                    title = "MOBPF-130",
                    description = link("https://jira.com/MOBPF-130"),
                    duration = "00:16:00",
                    entries = persistentListOf(
                        TrackedTasksListItem.Entry(
                            id = CompositeKey(5),
                            title = "MOBPF-130",
                            description = link("https://jira.com/MOBPF-130"),
                            duration = "00:09:00"
                        ),
                        TrackedTasksListItem.Entry(
                            id = CompositeKey(6),
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
        TrackedTasks(
            state = TrackedTasksViewState.Ok(
                items = days,
                entriesShownFor = persistentSetOf(days[0].items[1].id)
            ),
            onIntent = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}