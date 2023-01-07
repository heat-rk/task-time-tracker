package ru.heatrk.tasktimetracker.presentation.custom_composables.divider_surface

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.heatrk.tasktimetracker.presentation.values.dimens.InsetsDimens

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DividerSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    isDividerVisible: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        interactionSource = interactionSource,
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            content()

            if (isDividerVisible) {
                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = InsetsDimens.Default)
                )
            }
        }
    }
}