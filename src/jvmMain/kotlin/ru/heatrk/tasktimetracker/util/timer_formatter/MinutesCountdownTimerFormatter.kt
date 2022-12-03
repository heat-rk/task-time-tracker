package ru.heatrk.tasktimetracker.util.timer_formatter

import java.util.concurrent.TimeUnit

class MinutesCountdownTimerFormatter: TimerFormatter {
    override fun format(millis: Long): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)

        val mm = (seconds / TimeUnit.MINUTES.toSeconds(1) % TimeUnit.HOURS.toMinutes(1))
            .formatLeadingZero()

        val ss = (seconds % TimeUnit.MINUTES.toSeconds(1))
            .formatLeadingZero()

        return "$mm:$ss"
    }

    private fun Long.formatLeadingZero() = toString().padStart(2, ZERO_CHAR)

    companion object {
        private const val ZERO_CHAR = '0'
    }
}