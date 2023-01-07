package ru.heatrk.tasktimetracker.presentation.values.strings

object StringContainerRu: StringsContainer {
    override val taskName get() = "Название задачи"
    override val taskDescription get() = "Описание задачи"
    override val start get() = "Начать"
    override val stop get() = "Остановить"
    override val trackedTasksIsEmpty get() = "Список недавних задач пуст :("
    override val today get() = "Сегодня"
    override val yesterday get() = "Вчера"
}