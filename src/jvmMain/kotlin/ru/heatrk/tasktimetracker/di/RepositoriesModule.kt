package ru.heatrk.tasktimetracker.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import ru.heatrk.tasktimetracker.data.repositories.SettingsRepositoryImpl
import ru.heatrk.tasktimetracker.data.repositories.TrackedTasksRepositoryImpl
import ru.heatrk.tasktimetracker.domain.repositories.SettingsRepository
import ru.heatrk.tasktimetracker.domain.repositories.TrackedTasksRepository

val repositoriesModule = DI.Module("repositories") {
    bindProvider<SettingsRepository> { SettingsRepositoryImpl() }

    bindProvider<TrackedTasksRepository> {
        TrackedTasksRepositoryImpl(
            database = instance(),
            ioDispatcher = ioDispatcherInstance()
        )
    }
}