package ru.heatrk.tasktimetracker.di

import org.kodein.di.DI
import org.kodein.di.DirectDIAware
import org.kodein.di.bindProvider
import org.kodein.di.instance
import ru.heatrk.tasktimetracker.util.app_data.AppDataDirectoryProvider
import ru.heatrk.tasktimetracker.util.links.LinkClickHandler
import ru.heatrk.tasktimetracker.util.links.LinkClickHandlerImpl
import ru.heatrk.tasktimetracker.util.links.TextToLinkTextConverter
import ru.heatrk.tasktimetracker.util.links.TextToLinkTextConverterImpl
import ru.heatrk.tasktimetracker.util.time_formatter.local_date.LocalDateFormatter
import ru.heatrk.tasktimetracker.util.time_formatter.local_date.TrackedDayLocalDateFormatter
import ru.heatrk.tasktimetracker.util.time_formatter.millis.HhMmSsTimeFormatter
import ru.heatrk.tasktimetracker.util.time_formatter.millis.MillisecondsFormatter
import ru.heatrk.tasktimetracker.util.time_formatter.millis.MmSsTimeFormatter

private const val HH_MM_SS_TIME_FORMATTER = "hhMmSsTimeFormatter"
private const val MM_SS_TIME_FORMATTER = "mmSsTimeFormatter"
private const val TRACKED_DAY_DATE_FORMATTER = "dateFormatter"

val utilsModule = DI.Module("utilsModule") {
    bindProvider<MillisecondsFormatter>(tag = HH_MM_SS_TIME_FORMATTER) { HhMmSsTimeFormatter() }

    bindProvider<MillisecondsFormatter>(tag = MM_SS_TIME_FORMATTER) { MmSsTimeFormatter() }

    bindProvider<LocalDateFormatter>(tag = TRACKED_DAY_DATE_FORMATTER) {
        TrackedDayLocalDateFormatter()
    }

    bindProvider<LinkClickHandler> { LinkClickHandlerImpl() }

    bindProvider<TextToLinkTextConverter> {
        TextToLinkTextConverterImpl(
            defaultDispatcher = defaultDispatcherInstance(),
            linkClickHandler = instance()
        )
    }

    bindProvider { AppDataDirectoryProvider.create() }
}

fun DirectDIAware.hhMmSsTimeFormatterInstance() =
    instance<MillisecondsFormatter>(tag = HH_MM_SS_TIME_FORMATTER)

fun DirectDIAware.mmSsTimeFormatterInstance() =
    instance<MillisecondsFormatter>(tag = MM_SS_TIME_FORMATTER)

fun DirectDIAware.dateFormatterInstance() =
    instance<LocalDateFormatter>(tag = TRACKED_DAY_DATE_FORMATTER)

