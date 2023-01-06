package ru.heatrk.tasktimetracker.presentation.screens.tracker.tracked_tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.heatrk.tasktimetracker.presentation.custom_composables.links_text.LinksText
import ru.heatrk.tasktimetracker.presentation.values.dimens.ElementsDimens
import ru.heatrk.tasktimetracker.presentation.values.dimens.InsetsDimens
import ru.heatrk.tasktimetracker.presentation.values.styles.ApplicationTheme
import ru.heatrk.tasktimetracker.util.onlyTopCorners

@Composable
fun TrackedTasksDayHeader(
    dateTitle: String,
    totalTime: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = ApplicationTheme.shapes.medium.onlyTopCorners,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(InsetsDimens.Default),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = dateTitle, fontWeight = FontWeight.Bold)
            Text(text = totalTime, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TrackedTasksGroup(
    item: TrackedTasksListItem.Group,
    counterValue: Int,
    onClick: () -> Unit,
    isSelected: Boolean = false,
    shape: Shape = RectangleShape,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = shape,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(InsetsDimens.Default)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(ElementsDimens.EntriesGroupCounterSize)
                    .background(
                        shape = ApplicationTheme.shapes.medium,
                        color = if (isSelected) {
                            ApplicationTheme.colors.primary
                        } else {
                            Color.Transparent
                        }
                    )
                    .border(
                        shape = ApplicationTheme.shapes.medium,
                        color = if (isSelected) {
                            Color.Transparent
                        } else {
                            ApplicationTheme.colors.primary
                        },
                        width = ElementsDimens.BorderWidth
                    )
            ) {
                Text(
                    text = counterValue.toString(),
                    color = if (isSelected) {
                        ApplicationTheme.colors.onPrimary
                    } else {
                        ApplicationTheme.colors.onSurface
                    }
                )
            }

            Spacer(Modifier.width(InsetsDimens.Default))

            Column(modifier = Modifier.weight(1f)) {
                SelectionContainer {
                    Text(text = item.title)
                }

                SelectionContainer {
                    LinksText(item.description)
                }
            }

            Spacer(modifier = Modifier.height(InsetsDimens.Default))

            Text(text = item.duration)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TrackedTasksEntry(
    item: TrackedTasksListItem.Entry,
    isInner: Boolean = false,
    onClick: () -> Unit,
    shape: Shape = RectangleShape,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = shape,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(InsetsDimens.Default)
                .padding(
                    start = if (isInner) {
                        ElementsDimens.EntriesGroupCounterSize + InsetsDimens.Default
                    } else {
                        0.dp
                    }
                )
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SelectionContainer {
                    Text(text = item.title)
                }

                SelectionContainer {
                    LinksText(item.description)
                }
            }

            Spacer(modifier = Modifier.height(InsetsDimens.Default))

            Text(text = item.duration)
        }
    }
}