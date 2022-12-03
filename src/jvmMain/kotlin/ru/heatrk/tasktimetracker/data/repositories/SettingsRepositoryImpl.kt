package ru.heatrk.tasktimetracker.data.repositories

import ru.heatrk.tasktimetracker.domain.models.PomodoroConfig
import ru.heatrk.tasktimetracker.domain.repositories.SettingsRepository
import java.time.Duration

class SettingsRepositoryImpl: SettingsRepository {
    override suspend fun getPomodoroConfig(): PomodoroConfig {
        return PomodoroConfig(
            workingDuration = Duration.ofMinutes(30),
            shortChillDuration = Duration.ofMinutes(8),
            longChillDuration = Duration.ofMinutes(15),
            longChillPomodorosCount = 2
        )
    }
}