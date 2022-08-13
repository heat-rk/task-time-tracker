package ru.heatrk.tasktimetracker.presentation.root

import com.arkivanov.decompose.ComponentContext
import ru.heatrk.tasktimetracker.presentation.routing.Router

class RootComponent(
    componentContext: ComponentContext,
    val router: Router
): ComponentContext by componentContext