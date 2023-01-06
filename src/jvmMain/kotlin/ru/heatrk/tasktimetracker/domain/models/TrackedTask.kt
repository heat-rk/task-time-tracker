package ru.heatrk.tasktimetracker.domain.models

import java.time.Duration
import java.time.LocalDateTime

data class TrackedTask(
    val id: Int = -1,
    val title: String,
    val description: String,
    val duration: Duration,
    val startAt: LocalDateTime
)
