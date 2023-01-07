package ru.heatrk.tasktimetracker.util.time_formatter.local_date

import java.time.LocalDate

interface LocalDateFormatter {
    fun format(date: LocalDate): String
}