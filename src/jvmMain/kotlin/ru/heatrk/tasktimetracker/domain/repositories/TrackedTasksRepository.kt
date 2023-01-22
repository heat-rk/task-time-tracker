package ru.heatrk.tasktimetracker.domain.repositories

import ru.heatrk.tasktimetracker.domain.models.TrackedTask

interface TrackedTasksRepository {
    suspend fun addTrackedTask(trackedTask: TrackedTask): TrackedTask

    suspend fun deleteTrackedTasks(ids: List<Int>)

    suspend fun getTrackedTasks(): List<TrackedTask>
}