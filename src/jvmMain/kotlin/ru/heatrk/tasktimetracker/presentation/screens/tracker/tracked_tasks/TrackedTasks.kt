package ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import ru.heatrk.tasktimetracker.presentation.custom_composables.links_text.LinksText
import ru.heatrk.tasktimetracker.presentation.values.dimens.ElementsDimens
import ru.heatrk.tasktimetracker.presentation.values.dimens.InsetsDimens
import ru.heatrk.tasktimetracker.presentation.values.styles.ApplicationTheme
import ru.heatrk.tasktimetracker.util.links.LinksTextValue

@Composable
fun TrackedTasks(
    state: TrackedTasksViewState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    when (state) {
        is TrackedTasksViewState.Ok -> TrackedTasksOkState(state, contentPadding, modifier)
        is TrackedTasksViewState.Error -> TrackedTasksErrorState(state, modifier)
        TrackedTasksViewState.Loading -> TrackedTasksLoadingState(modifier)
    }
}

@Composable
private fun TrackedTasksOkState(
    state: TrackedTasksViewState.Ok,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        itemsIndexed(state.days) {index, trackedDay ->
            TrackedDay(
                dayItem = trackedDay,
                modifier = Modifier.fillMaxWidth()
            )

            if (index != state.days.lastIndex) {
                Spacer(modifier = Modifier.height(InsetsDimens.Default))
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

@Composable
private fun TrackedDay(
    dayItem: TrackedDayItem,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = ApplicationTheme.shapes.medium
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(InsetsDimens.Default)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = dayItem.title, fontWeight = FontWeight.Bold)
                Text(text = dayItem.totalTime, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(InsetsDimens.ExtraLarge))

            dayItem.items.forEachIndexed { index, item ->
                TrackedTaskEntry(
                    task = item,
                    isLast = index == dayItem.items.lastIndex,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun TrackedTaskEntry(
    task: TrackedTaskItem,
    isLast: Boolean = false,
    contentStartPadding: Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = InsetsDimens.Default,
                    bottom = InsetsDimens.Default,
                    start = contentStartPadding
                )
        ) {
            if (task is TrackedTaskItem.Group) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(ElementsDimens.EntriesGroupCounterSize)
                        .background(
                            shape = ApplicationTheme.shapes.medium,
                            color = if (task.isEntriesShown) {
                                ApplicationTheme.colors.primary
                            } else {
                                Color.Transparent
                            }
                        )
                        .border(
                            shape = ApplicationTheme.shapes.medium,
                            color = if (task.isEntriesShown) {
                                Color.Transparent
                            } else {
                                ApplicationTheme.colors.primary
                            },
                            width = ElementsDimens.BorderWidth
                        )
                ) {
                    Text(
                        text = task.entries.size.toString(),
                        color = if (task.isEntriesShown) {
                            ApplicationTheme.colors.onPrimary
                        } else {
                            ApplicationTheme.colors.onSurface
                        }
                    )
                }

                Spacer(Modifier.width(InsetsDimens.Default))
            }

            Column(modifier = Modifier.weight(1f)) {
                SelectionContainer {
                    Text(text = task.title)
                }

                SelectionContainer {
                    LinksText(task.description)
                }
            }

            Spacer(modifier = Modifier.height(InsetsDimens.Default))

            Text(text = task.duration)
        }

        if (!isLast || (task is TrackedTaskItem.Group && task.isEntriesShown)) {
            Divider(modifier = Modifier.fillMaxWidth())
        }

        if (task is TrackedTaskItem.Group && task.isEntriesShown) {
            task.entries.forEachIndexed { index, item ->
                TrackedTaskEntry(
                    task = item,
                    isLast = index == task.entries.lastIndex,
                    contentStartPadding = ElementsDimens.EntriesGroupCounterSize + InsetsDimens.Default,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
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
            items = listOf(
                TrackedTaskItem.Entry(
                    title = "MOBPF-128",
                    description = link("https://jira.com/MOBPF-128"),
                    duration = "00:12:36"
                ),
                TrackedTaskItem.Group(
                    title = "MOBPF-130",
                    description = link("https://jira.com/MOBPF-130"),
                    duration = "00:16:00",
                    isEntriesShown = true,
                    entries = listOf(
                        TrackedTaskItem.Entry(
                            title = "MOBPF-130",
                            description = link("https://jira.com/MOBPF-130"),
                            duration = "00:09:00"
                        ),
                        TrackedTaskItem.Entry(
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
            items = listOf(
                TrackedTaskItem.Entry(
                    title = "MOBPF-128",
                    description = link("https://jira.com/MOBPF-128"),
                    duration = "00:12:36"
                ),
                TrackedTaskItem.Group(
                    title = "MOBPF-130",
                    description = link("https://jira.com/MOBPF-130"),
                    duration = "00:16:00",
                    entries = listOf(
                        TrackedTaskItem.Entry(
                            title = "MOBPF-130",
                            description = link("https://jira.com/MOBPF-130"),
                            duration = "00:09:00"
                        ),
                        TrackedTaskItem.Entry(
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
            state = TrackedTasksViewState.Ok(days),
            modifier = Modifier.fillMaxWidth()
        )
    }
}