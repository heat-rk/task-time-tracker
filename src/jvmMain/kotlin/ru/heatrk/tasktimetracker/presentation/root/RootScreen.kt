package ru.heatrk.tasktimetracker.presentation.root

import androidx.compose.runtime.Composable

@Composable
fun RootScreen(component: RootComponent) {
    component.router.Container()
}