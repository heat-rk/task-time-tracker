package ru.heatrk.tasktimetracker.domain.usecases

import ru.heatrk.tasktimetracker.domain.models.PomodoroConfig
import ru.heatrk.tasktimetracker.domain.repositories.SettingsRepository

class GetPomodoroConfigUseCase(
    private val settingsRepository: SettingsRepository
): UseCase<PomodoroConfig, Unit> {
    override suspend fun invoke(params: Unit) =
        settingsRepository.getPomodoroConfig()
}