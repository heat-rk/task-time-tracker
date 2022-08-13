// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
@file:OptIn(ExperimentalDecomposeApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.ExperimentalDecomposeApi
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.withDI
import ru.heatrk.tasktimetracker.di.modules
import ru.heatrk.tasktimetracker.presentation.root.RootComponent
import ru.heatrk.tasktimetracker.presentation.root.RootScreen
import ru.heatrk.tasktimetracker.presentation.strings.strings

@Composable
fun App(rootComponent: RootComponent)  {
    RootScreen(rootComponent)
}

fun main() = application {
    withDI(*modules) {
        val rootComponent: RootComponent by rememberInstance("rootComponent")

        Window(
            onCloseRequest = ::exitApplication,
            title = strings.applicationName
        ) {
            App(rootComponent)
        }
    }
}