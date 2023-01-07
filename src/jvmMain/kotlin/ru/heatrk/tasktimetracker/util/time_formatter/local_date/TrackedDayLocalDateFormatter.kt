package ru.heatrk.tasktimetracker.util.time_formatter.local_date

import ru.heatrk.tasktimetracker.presentation.values.strings.strings
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TrackedDayLocalDateFormatter: LocalDateFormatter {
    override fun format(date: LocalDate): String {
        val now = LocalDate.now()

        return when (date) {
            now -> {
                strings.today
            }
            now.minusDays(1) -> {
                strings.yesterday
            }
            else -> {
                date.format(DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN))
            }
        }
    }

    companion object {
        private const val DATE_FORMATTER_PATTERN = "dd MMMM yyyy"
    }
}