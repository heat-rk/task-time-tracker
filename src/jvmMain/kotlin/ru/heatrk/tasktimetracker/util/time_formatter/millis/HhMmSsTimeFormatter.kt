package ru.heatrk.tasktimetracker.util.time_formatter.millis

import java.util.concurrent.TimeUnit

class HhMmSsTimeFormatter: MillisecondsFormatter {
    override fun format(millis: Long): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)

        val hh = seconds / TimeUnit.HOURS.toSeconds(1)

        val mm = (seconds / TimeUnit.MINUTES.toSeconds(1) % TimeUnit.HOURS.toMinutes(1))
            .formatLeadingZero()

        val ss = (seconds % TimeUnit.MINUTES.toSeconds(1))
            .formatLeadingZero()

        return "$hh:$mm:$ss"
    }

    private fun Long.formatLeadingZero() = toString().padStart(2, ZERO_CHAR)

    companion object {
        private const val ZERO_CHAR = '0'
    }
}