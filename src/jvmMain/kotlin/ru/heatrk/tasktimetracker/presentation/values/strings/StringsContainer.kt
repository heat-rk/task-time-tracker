package ru.heatrk.tasktimetracker.presentation.values.strings

import java.util.*

val strings get() = when(Locale.getDefault().language) {
    "ru" -> StringContainerRu
    "en" -> StringContainerEn
    else -> StringContainerRu
}

interface StringsContainer {
    val applicationName: String get() = "Task Time Tracker!"
    val taskName: String
    val taskDescription: String
    val start: String
    val stop: String
    val trackedTasksIsEmpty: String
    val today: String
    val yesterday: String
}