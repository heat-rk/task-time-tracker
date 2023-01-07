package ru.heatrk.tasktimetracker.util

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

val CornerBasedShape.onlyTopCorners get() =
    copy(
        topStart = topStart,
        topEnd = topEnd,
        bottomStart = CornerSize(0.dp),
        bottomEnd = CornerSize(0.dp)
    )

val CornerBasedShape.onlyBottomCorners get() =
    copy(
        topStart = CornerSize(0.dp),
        topEnd = CornerSize(0.dp),
        bottomStart = bottomStart,
        bottomEnd = bottomEnd
    )

suspend fun MutableInteractionSource.emitClick(offset: Offset) {
    val press = PressInteraction.Press(offset)
    emit(press)
    delay(30)
    emit(PressInteraction.Release(press))
}