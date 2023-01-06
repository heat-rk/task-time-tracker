package ru.heatrk.tasktimetracker.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.DirectDIAware
import org.kodein.di.bindProvider
import org.kodein.di.instance

private const val IO_DISPATCHER = "io_dispatcher"
private const val DEFAULT_DISPATCHER = "default_dispatcher"
private const val MAIN_DISPATCHER = "main_dispatcher"

val dispatchersModule = DI.Module("dispatchersModule") {
    bindProvider(tag = IO_DISPATCHER) { Dispatchers.IO }
    bindProvider(tag = DEFAULT_DISPATCHER) { Dispatchers.Default }
    bindProvider(tag = MAIN_DISPATCHER) { Dispatchers.Main }
}

fun DirectDIAware.ioDispatcherInstance() = instance<CoroutineDispatcher>(IO_DISPATCHER)
fun DirectDIAware.defaultDispatcherInstance() = instance<CoroutineDispatcher>(DEFAULT_DISPATCHER)
fun DirectDIAware.mainDispatcherInstance() = instance<CoroutineDispatcher>(MAIN_DISPATCHER)