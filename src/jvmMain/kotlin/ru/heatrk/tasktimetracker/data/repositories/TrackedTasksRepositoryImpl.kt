package ru.heatrk.tasktimetracker.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.heatrk.tasktimetracker.data.database.tables.TrackedTaskDao
import ru.heatrk.tasktimetracker.data.database.tables.TrackedTaskTable
import ru.heatrk.tasktimetracker.domain.models.TrackedTask
import ru.heatrk.tasktimetracker.domain.repositories.TrackedTasksRepository
import java.time.*

class TrackedTasksRepositoryImpl(
    private val database: Database,
    private val ioDispatcher: CoroutineDispatcher
): TrackedTasksRepository {

    override suspend fun addTrackedTask(trackedTask: TrackedTask) {
        newSuspendedTransaction(context = ioDispatcher, db = database) {
            SchemaUtils.create(TrackedTaskTable)

            TrackedTaskDao.new {
                title = trackedTask.title
                description = trackedTask.description
                duration = trackedTask.duration.seconds
                startAt = trackedTask.startAt
                    .atZone(ZoneOffset.systemDefault())
                    .toInstant()
                    .epochSecond
            }
        }
    }

    override suspend fun deleteTrackedTasks(ids: List<Int>) {
        newSuspendedTransaction(context = ioDispatcher, db = database) {
            SchemaUtils.create(TrackedTaskTable)

            TrackedTaskDao.forIds(ids).forEach { it.delete() }
        }
    }

    override suspend fun getTrackedTasks() =
        newSuspendedTransaction(context = ioDispatcher, db = database) {
            SchemaUtils.create(TrackedTaskTable)

            TrackedTaskDao.all().sortedByDescending { it.startAt }.map {
                TrackedTask(
                    id = it.id.value,
                    title = it.title,
                    description = it.description,
                    duration = Duration.ofSeconds(it.duration),
                    startAt = LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(it.startAt),
                        ZoneId.systemDefault()
                    )
                )
            }
        }
}