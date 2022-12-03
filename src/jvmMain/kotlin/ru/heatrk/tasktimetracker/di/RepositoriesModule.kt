package ru.heatrk.tasktimetracker.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import ru.heatrk.tasktimetracker.data.repositories.SettingsRepositoryImpl
import ru.heatrk.tasktimetracker.domain.repositories.SettingsRepository

val repositoriesModule = DI.Module("repositories") {
    bindProvider<SettingsRepository> { SettingsRepositoryImpl() }
}