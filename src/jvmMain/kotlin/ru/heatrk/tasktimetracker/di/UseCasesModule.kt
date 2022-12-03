package ru.heatrk.tasktimetracker.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import ru.heatrk.tasktimetracker.domain.models.PomodoroConfig
import ru.heatrk.tasktimetracker.domain.usecases.GetPomodoroConfigUseCase
import ru.heatrk.tasktimetracker.domain.usecases.UseCase

val useCasesModule = DI.Module("useCases") {
    bindProvider<UseCase<PomodoroConfig, Unit>> {
        GetPomodoroConfigUseCase(
            settingsRepository = instance()
        )
    }
}