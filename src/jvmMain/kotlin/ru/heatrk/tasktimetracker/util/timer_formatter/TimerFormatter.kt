package ru.heatrk.tasktimetracker.util.timer_formatter

interface TimerFormatter {
    fun format(millis: Long): String
}