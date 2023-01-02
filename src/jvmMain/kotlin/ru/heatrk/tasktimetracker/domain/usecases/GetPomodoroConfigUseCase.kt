package ru.heatrk.tasktimetracker.domain.usecases

import ru.heatrk.tasktimetracker.domain.repositories.SettingsRepository

class GetPomodoroConfigUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke() =
        settingsRepository.getPomodoroConfig()
}