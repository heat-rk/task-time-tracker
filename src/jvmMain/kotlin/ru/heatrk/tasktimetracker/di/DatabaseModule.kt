package ru.heatrk.tasktimetracker.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.ktorm.database.Database

val databaseModule = DI.Module("databaseModule") {
    bindSingleton {
        Database.connect("jdbc:sqlite:tasktimetracker.db")
    }
}