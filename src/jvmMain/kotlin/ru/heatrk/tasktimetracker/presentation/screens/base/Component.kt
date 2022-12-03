package ru.heatrk.tasktimetracker.presentation.screens.base

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

abstract class Component(
    componentContext: ComponentContext
): ComponentContext by componentContext {
    protected val componentScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        onCreate()
    }

    private fun onCreate() {
        lifecycle.doOnDestroy {
            onDestroy()
            componentScope.coroutineContext.cancelChildren()
        }
    }

    open fun onDestroy() = Unit
}