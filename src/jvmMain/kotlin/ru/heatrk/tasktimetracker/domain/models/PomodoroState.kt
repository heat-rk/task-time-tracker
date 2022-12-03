package ru.heatrk.tasktimetracker.domain.models

enum class PomodoroState {
    WORKING, CHILL_SHORT, CHILL_LONG;

    fun duration(config: PomodoroConfig) = when (this) {
        WORKING -> config.workingDuration
        CHILL_SHORT -> config.shortChillDuration
        CHILL_LONG -> config.longChillDuration
    }
}