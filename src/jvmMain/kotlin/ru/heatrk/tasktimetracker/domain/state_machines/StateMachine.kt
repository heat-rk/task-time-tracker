package ru.heatrk.tasktimetracker.domain.state_machines

import kotlinx.coroutines.flow.StateFlow

interface StateMachine<T, S> {
    val state: StateFlow<T>
    fun produceNewState(sideEffect: S)
}