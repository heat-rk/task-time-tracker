package ru.heatrk.tasktimetracker.data.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object TrackedTaskTable: IntIdTable(name = "t_tracked_task", columnName = "f_id") {
    val title = varchar(name = "f_title", length = 256)
    val description = varchar(name = "f_description", length = 1024)
    val duration = long("f_duration")
    val startAt = long("f_start_at")
}