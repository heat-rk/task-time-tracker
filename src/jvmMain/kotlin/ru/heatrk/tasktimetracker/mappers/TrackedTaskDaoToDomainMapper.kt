package ru.heatrk.tasktimetracker.mappers

import ru.heatrk.tasktimetracker.data.database.tables.TrackedTaskDao
import ru.heatrk.tasktimetracker.domain.models.TrackedTask
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TrackedTaskDaoToDomainMapper {
    fun map(dao: TrackedTaskDao) = TrackedTask(
        id = dao.id.value,
        title = dao.title,
        description = dao.description,
        duration = Duration.ofSeconds(dao.duration),
        startAt = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(dao.startAt),
            ZoneId.systemDefault()
        )
    )
}