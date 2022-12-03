package ru.heatrk.tasktimetracker.presentation.screens.tracker.timer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import ru.heatrk.tasktimetracker.presentation.values.dimens.ElementsDimens
import ru.heatrk.tasktimetracker.presentation.values.dimens.InsetsDimens
import ru.heatrk.tasktimetracker.presentation.values.dimens.TextSizeDimens
import ru.heatrk.tasktimetracker.presentation.values.images.Drawables
import ru.heatrk.tasktimetracker.presentation.values.strings.strings

@Composable
fun TaskTimer(
    state: TaskTimerViewState,
    onIntent: (TaskTimerIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            TextField(
                value = state.taskName,
                onValueChange = { onIntent(TaskTimerIntent.OnTaskNameChange(it)) },
                label = { Text(text = strings.taskName) },
                enabled = state.isEnabled,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(InsetsDimens.Default))

            TextField(
                value = state.taskUrl,
                onValueChange = { onIntent(TaskTimerIntent.OnTaskUrlChange(it)) },
                label = { Text(text = strings.taskUrl) },
                enabled = state.isEnabled,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.width(InsetsDimens.Default))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = InsetsDimens.ExtraLarge)
        ) {
            val buttonPainter: Painter
            val buttonContentDescription: String
            val buttonIntent: TaskTimerIntent

            if (state.isRunning) {
                buttonPainter = painterResource(Drawables.StopIcon)
                buttonContentDescription = strings.stop
                buttonIntent = TaskTimerIntent.OnStopButtonClick
            } else {
                buttonPainter = painterResource(Drawables.StartIcon)
                buttonContentDescription = strings.start
                buttonIntent = TaskTimerIntent.OnStartButtonClick
            }

            IconButton(
                onClick = { onIntent(buttonIntent) },
                enabled = state.isEnabled,
                modifier = Modifier.size(ElementsDimens.StartStopButtonSize)
            ) {
                Icon(
                    painter = buttonPainter,
                    contentDescription = buttonContentDescription
                )
            }

            Spacer(modifier = Modifier.height(InsetsDimens.Default))

            Text(
                text = state.timePassed,
                fontSize = TextSizeDimens.ExtraLarge
            )
        }
    }
}

@Composable
@Preview
private fun TaskTimerPreview() {
    TaskTimer(
        state = TaskTimerViewState(timePassed = "00:12:45"),
        onIntent = {},
        modifier = Modifier.fillMaxWidth()
    )
}