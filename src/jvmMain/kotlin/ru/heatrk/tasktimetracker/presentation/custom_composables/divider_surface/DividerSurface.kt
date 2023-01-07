package ru.heatrk.tasktimetracker.presentation.custom_composables.divider_surface

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import ru.heatrk.tasktimetracker.presentation.values.dimens.InsetsDimens

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DividerSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    isDividerVisible: Boolean = true,
    content: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
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