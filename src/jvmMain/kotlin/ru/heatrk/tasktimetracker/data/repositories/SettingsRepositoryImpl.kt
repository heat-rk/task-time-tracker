package ru.heatrk.tasktimetracker.data.repositories

import ru.heatrk.tasktimetracker.domain.models.PomodoroConfig
import ru.heatrk.tasktimetracker.domain.repositories.SettingsRepository
import java.time.Duration

class SettingsRepositoryImpl: SettingsRepository {
    override suspend fun getPomodoroConfig(): PomodoroConfig {
        return PomodoroConfig(
            workingDuration = Duration.ofSeconds(15),
            shortChillDuration = Duration.ofSeconds(5),
            longChillDuration = Duration.ofSeconds(10),
            longChillPomodorosCount = 2
        )
    }
}