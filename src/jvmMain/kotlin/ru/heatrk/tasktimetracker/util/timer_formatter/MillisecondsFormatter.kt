package ru.heatrk.tasktimetracker.util.timer_formatter

interface MillisecondsFormatter {
    fun format(millis: Long): String
}