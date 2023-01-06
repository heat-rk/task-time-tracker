package ru.heatrk.tasktimetracker.util

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.unit.dp

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