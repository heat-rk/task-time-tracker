package ru.heatrk.tasktimetracker.domain.state_machines

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.heatrk.tasktimetracker.domain.models.PomodoroConfig
import ru.heatrk.tasktimetracker.domain.models.PomodoroState

class PomodoroStateMachine(
    private val config: PomodoroConfig
): StateMachine<PomodoroState, Unit> {
    private var pomodorosCount = 0

    private val _state = MutableStateFlow(PomodoroState.WORKING)
    override val state = _state.asStateFlow()

    override fun produceNewState(sideEffect: Unit) {
        val state = state.value

        val nextState = when {
            pomodorosCount < config.longChillPomodorosCount && state == PomodoroState.WORKING -> {
                pomodorosCount++
                PomodoroState.CHILL_SHORT
            }

            pomodorosCount == config.longChillPomodorosCount && state == PomodoroState.WORKING -> {
                pomodorosCount = 0
                PomodoroState.CHILL_LONG
            }

            else -> {
                PomodoroState.WORKING
            }
        }

        _state.value = nextState
    }
}