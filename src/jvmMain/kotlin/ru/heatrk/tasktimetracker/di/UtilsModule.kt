package ru.heatrk.tasktimetracker.di

import org.kodein.di.DI
import org.kodein.di.DirectDIAware
import org.kodein.di.bindProvider
import org.kodein.di.instance
import ru.heatrk.tasktimetracker.util.links.LinkClickHandler
import ru.heatrk.tasktimetracker.util.links.LinkClickHandlerImpl
import ru.heatrk.tasktimetracker.util.links.TextToLinkTextConverter
import ru.heatrk.tasktimetracker.util.links.TextToLinkTextConverterImpl
import ru.heatrk.tasktimetracker.util.timer_formatter.MmSsTimeFormatter
import ru.heatrk.tasktimetracker.util.timer_formatter.HhMmSsTimeFormatter
import ru.heatrk.tasktimetracker.util.timer_formatter.MillisecondsFormatter
import java.time.format.DateTimeFormatter

private const val HH_MM_SS_TIME_FORMATTER = "hhMmSsTimeFormatter"
private const val MM_SS_TIME_FORMATTER = "mmSsTimeFormatter"
private const val DATE_FORMATTER = "dateFormatter"

private const val DATE_FORMATTER_PATTERN = "dd MMMM yyyy"

val utilsModule = DI.Module("utilsModule") {
    bindProvider<MillisecondsFormatter>(tag = HH_MM_SS_TIME_FORMATTER) { HhMmSsTimeFormatter() }

    bindProvider<MillisecondsFormatter>(tag = MM_SS_TIME_FORMATTER) { MmSsTimeFormatter() }

    bindProvider<DateTimeFormatter>(tag = DATE_FORMATTER) { DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN) }

    bindProvider<LinkClickHandler> { LinkClickHandlerImpl() }

    bindProvider<TextToLinkTextConverter> {
        TextToLinkTextConverterImpl(
            defaultDispatcher = defaultDispatcherInstance(),
            linkClickHandler = instance()
        )
    }
}

fun DirectDIAware.hhMmSsTimeFormatterInstance() =
    instance<MillisecondsFormatter>(tag = HH_MM_SS_TIME_FORMATTER)

fun DirectDIAware.mmSsTimeFormatterInstance() =
    instance<MillisecondsFormatter>(tag = MM_SS_TIME_FORMATTER)

fun DirectDIAware.dateFormatterInstance() =
    instance<DateTimeFormatter>(tag = DATE_FORMATTER)

