package ru.heatrk.tasktimetracker.util.time_formatter.millis

interface MillisecondsFormatter {
    fun format(millis: Long): String
}