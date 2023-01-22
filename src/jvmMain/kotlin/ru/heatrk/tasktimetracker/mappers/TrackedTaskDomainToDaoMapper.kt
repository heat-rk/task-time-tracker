package ru.heatrk.tasktimetracker.mappers

import ru.heatrk.tasktimetracker.data.database.tables.TrackedTaskDao
import ru.heatrk.tasktimetracker.domain.models.TrackedTask
import java.time.ZoneOffset

class TrackedTaskDomainToDaoMapper {
    fun map(task: TrackedTask)  = TrackedTaskDao.new {
        title = task.title
        description = task.description
        duration = task.duration.seconds
        startAt = task.startAt
            .atZone(ZoneOffset.systemDefault())
            .toInstant()
            .epochSecond
    }
}