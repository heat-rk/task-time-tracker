package ru.heatrk.tasktimetracker.domain.models

import java.time.Duration

data class PomodoroConfig(
    val workingDuration: Duration,
    val shortChillDuration: Duration,
    val longChillDuration: Duration,
    val longChillPomodorosCount: Int
)
