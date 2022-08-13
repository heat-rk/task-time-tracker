package ru.heatrk.tasktimetracker.presentation.strings

import java.util.*

val strings get() = when(Locale.getDefault().language) {
    "ru" -> StringContainerRu
    else -> StringContainerRu
}

interface StringsContainer {
    val applicationName: String get() = "Task Time Tracker!"
}