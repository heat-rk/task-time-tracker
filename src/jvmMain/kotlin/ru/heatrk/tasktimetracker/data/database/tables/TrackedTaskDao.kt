package ru.heatrk.tasktimetracker.data.database.tables

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TrackedTaskDao(id: EntityID<Int>): IntEntity(id) {
    var title by TrackedTaskTable.title
    var description by TrackedTaskTable.description
    var duration by TrackedTaskTable.duration
    var startAt by TrackedTaskTable.startAt

    companion object : IntEntityClass<TrackedTaskDao>(TrackedTaskTable)
}