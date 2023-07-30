package ru.heatrk.tasktimetracker.data.repositories

import ru.heatrk.tasktimetracker.BuildConfig
import ru.heatrk.tasktimetracker.domain.models.PomodoroConfig
import ru.heatrk.tasktimetracker.domain.repositories.SettingsRepository
import java.time.Duration

class SettingsRepositoryImpl: SettingsRepository {
    override suspend fun getPomodoroConfig(): PomodoroConfig {
        val workingDurationSeconds = BuildConfig.WORKING_DURATION_SECONDS_DEFAULT.toLong()
        val shortChillDurationSeconds = BuildConfig.SHORT_CHILL_SECONDS_DEFAULT.toLong()
        val longChillDurationSeconds = BuildConfig.LONG_CHILL_SECONDS_DEFAULT.toLong()
        val longChillPomodorosCount = BuildConfig.LONG_CHILL_POMODOROS_COUNT

        return PomodoroConfig(
            workingDuration = Duration.ofSeconds(workingDurationSeconds),
            shortChillDuration = Duration.ofSeconds(shortChillDurationSeconds),
            longChillDuration = Duration.ofSeconds(longChillDurationSeconds),
            longChillPomodorosCount = longChillPomodorosCount
        )
    }
}