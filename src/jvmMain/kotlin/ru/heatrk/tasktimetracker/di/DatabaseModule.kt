package ru.heatrk.tasktimetracker.di

import org.jetbrains.exposed.sql.Database
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val databaseModule = DI.Module("databaseModule") {
    bindSingleton {
        Database.connect("jdbc:sqlite:tasktimetracker.db")
    }
}