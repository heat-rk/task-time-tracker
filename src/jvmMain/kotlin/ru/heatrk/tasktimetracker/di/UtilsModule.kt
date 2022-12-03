package ru.heatrk.tasktimetracker.di

import org.kodein.di.DI
import org.kodein.di.DirectDIAware
import org.kodein.di.bindProvider
import org.kodein.di.instance
import ru.heatrk.tasktimetracker.util.timer_formatter.MinutesCountdownTimerFormatter
import ru.heatrk.tasktimetracker.util.timer_formatter.StopWatchTimerFormatter
import ru.heatrk.tasktimetracker.util.timer_formatter.TimerFormatter

private const val STOP_WATCH_TIMER_FORMATTER = "stopWatchTimerFormatter"
private const val MINUTES_COUNTDOWN_TIMER_FORMATTER = "minutesCountdownTimerFormatter"

val utilsModule = DI.Module("utilsModule") {
    bindProvider<TimerFormatter>(tag = STOP_WATCH_TIMER_FORMATTER) { StopWatchTimerFormatter() }
    bindProvider<TimerFormatter>(tag = MINUTES_COUNTDOWN_TIMER_FORMATTER) { MinutesCountdownTimerFormatter() }
}

fun DirectDIAware.stopWatchTimerFormatterInstance() =
    instance<TimerFormatter>(tag = STOP_WATCH_TIMER_FORMATTER)

fun DirectDIAware.minutesCountdownTimerFormatterInstance() =
    instance<TimerFormatter>(tag = MINUTES_COUNTDOWN_TIMER_FORMATTER)

