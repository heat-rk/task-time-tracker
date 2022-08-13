package ru.heatrk.tasktimetracker.presentation.routing

import androidx.compose.runtime.Composable

interface Router {
    @Composable
    fun Container()

    fun navigateUp()
}