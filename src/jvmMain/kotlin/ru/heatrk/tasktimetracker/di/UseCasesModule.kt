package ru.heatrk.tasktimetracker.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import ru.heatrk.tasktimetracker.domain.usecases.GetPomodoroConfigUseCase

val useCasesModule = DI.Module("useCases") {
    bindProvider {
        GetPomodoroConfigUseCase(
            settingsRepository = instance()
        )
    }
}