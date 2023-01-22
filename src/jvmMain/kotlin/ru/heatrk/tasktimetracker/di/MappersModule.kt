package ru.heatrk.tasktimetracker.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import ru.heatrk.tasktimetracker.mappers.TrackedTaskDaoToDomainMapper
import ru.heatrk.tasktimetracker.mappers.TrackedTaskDomainToDaoMapper
import ru.heatrk.tasktimetracker.mappers.TrackedTaskDomainToListItemsMapper

val mappersModule = DI.Module("mappersModule") {
    bindProvider {
        TrackedTaskDomainToListItemsMapper(
            totalTimeFormatter = hhMmSsTimeFormatterInstance(),
            textToLinkTextConverter = instance(),
            dateFormatter = dateFormatterInstance()
        )
    }

    bindProvider {
        TrackedTaskDomainToDaoMapper()
    }

    bindProvider {
        TrackedTaskDaoToDomainMapper()
    }
}