package ru.heatrk.tasktimetracker.domain.repositories

import ru.heatrk.tasktimetracker.domain.models.PomodoroConfig

interface SettingsRepository {
    suspend fun getPomodoroConfig(): PomodoroConfig
}