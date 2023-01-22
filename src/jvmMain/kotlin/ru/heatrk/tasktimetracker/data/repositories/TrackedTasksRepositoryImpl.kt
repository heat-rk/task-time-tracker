package ru.heatrk.tasktimetracker.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.heatrk.tasktimetracker.data.database.tables.TrackedTaskDao
import ru.heatrk.tasktimetracker.data.database.tables.TrackedTaskTable
import ru.heatrk.tasktimetracker.domain.models.TrackedTask
import ru.heatrk.tasktimetracker.domain.repositories.TrackedTasksRepository
import ru.heatrk.tasktimetracker.mappers.TrackedTaskDaoToDomainMapper
import ru.heatrk.tasktimetracker.mappers.TrackedTaskDomainToDaoMapper

class TrackedTasksRepositoryImpl(
    private val database: Database,
    private val ioDispatcher: CoroutineDispatcher,
    private val trackedTaskDaoToDomainMapper: TrackedTaskDaoToDomainMapper,
    private val trackedTaskDomainToDaoMapper: TrackedTaskDomainToDaoMapper,
): TrackedTasksRepository {

    override suspend fun addTrackedTask(trackedTask: TrackedTask) =
        newSuspendedTransaction(context = ioDispatcher, db = database) {
            SchemaUtils.create(TrackedTaskTable)

            trackedTaskDaoToDomainMapper.map(trackedTaskDomainToDaoMapper.map(trackedTask))
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
                trackedTaskDaoToDomainMapper.map(it)
            }
        }
}