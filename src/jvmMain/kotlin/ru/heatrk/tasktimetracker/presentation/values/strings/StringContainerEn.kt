package ru.heatrk.tasktimetracker.presentation.values.strings

object StringContainerEn: StringsContainer {
    override val taskName get() = "Task name"
    override val taskDescription get() = "Task description"
    override val start get() = "Start"
    override val stop get() = "Stop"
    override val trackedTasksIsEmpty get() = "There are no recent entries :("
    override val today get() = "Today"
    override val yesterday get() = "Yesterday"
}