package ru.heatrk.tasktimetracker.di

import org.jetbrains.exposed.sql.Database
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ru.heatrk.tasktimetracker.util.app_data.AppDataDirectoryProvider

val databaseModule = DI.Module("databaseModule") {
    bindSingleton {
        val appDataProvider = instance<AppDataDirectoryProvider>()
        Database.connect("jdbc:sqlite:${appDataProvider.providePath()}/tasktimetracker.db")
    }
}