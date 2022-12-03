package ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.heatrk.tasktimetracker.domain.models.PomodoroConfig
import ru.heatrk.tasktimetracker.domain.models.PomodoroState
import ru.heatrk.tasktimetracker.domain.state_machines.PomodoroStateMachine
import ru.heatrk.tasktimetracker.domain.usecases.UseCase
import ru.heatrk.tasktimetracker.presentation.screens.base.Component
import ru.heatrk.tasktimetracker.presentation.screens.tracker.TimerStartListener
import ru.heatrk.tasktimetracker.presentation.screens.tracker.TimerStopListener
import ru.heatrk.tasktimetracker.util.fromToTickerFlow
import ru.heatrk.tasktimetracker.util.timer_formatter.TimerFormatter
import java.util.concurrent.TimeUnit

class PomodoroTimerComponent(
    componentContext: ComponentContext,
    private val getPomodoroConfigUseCase: UseCase<PomodoroConfig, Unit>,
    private val timerFormatter: TimerFormatter
): Component(componentContext), TimerStartListener, TimerStopListener {

    private var timerJob: Job? = null

    private var _pomodoroConfig: PomodoroConfig? = null
    private val pomodoroConfig get() = requireNotNull(_pomodoroConfig)

    private var _pomodoroStateMachine: PomodoroStateMachine? = null
    private val pomodoroStateMachine get() = requireNotNull(_pomodoroStateMachine)

    private val stopListeners = mutableSetOf<TimerStopListener>()
    private val stateChangedListeners = mutableSetOf<PomodoroStateChangedListener>()

    private var remainingTimeInMillis = 0L
        set(value) {
            field = value
            _state.update { it.copy(remainingTime = timerFormatter.format(value)) }
        }

    private val _state = MutableStateFlow(PomodoroTimerViewState(
        remainingTime = timerFormatter.format(remainingTimeInMillis)
    ))

    val state = _state.asStateFlow()

    init {
        componentScope.launch {
            _pomodoroConfig = getPomodoroConfigUseCase(Unit)
            _pomodoroStateMachine = PomodoroStateMachine(pomodoroConfig)

            remainingTimeInMillis = pomodoroStateMachine.state.value
                .duration(pomodoroConfig)
                .toMillis()

            pomodoroStateMachine.state
                .onEach { state ->
                    remainingTimeInMillis = state.duration(pomodoroConfig).toMillis()

                    when (state) {
                        PomodoroState.CHILL_SHORT -> {
                            _state.update { it.copy(isChilling = true) }
                            triggerAllStopListeners()
                            onStart()
                        }
                        PomodoroState.CHILL_LONG -> {
                            _state.update { it.copy(isChilling = true) }
                            triggerAllStopListeners()
                            onStart()
                        }
                        PomodoroState.WORKING -> {
                            _state.update { it.copy(isChilling = false) }
                        }
                    }

                    triggerAllStateChangedListeners(state)
                }.launchIn(this)
        }
    }

    override fun onStart() {
        val secondDelay = TimeUnit.SECONDS.toMillis(1)

        timerJob = fromToTickerFlow(
            from = remainingTimeInMillis,
            to = 0L,
            step = TimeUnit.SECONDS.toMillis(1),
            period = secondDelay,
            initialDelay = secondDelay
        ).flowOn(Dispatchers.Default).onEach {
            remainingTimeInMillis = it
        }.onCompletion {
            if (remainingTimeInMillis == 0L) {
                pomodoroStateMachine.produceNewState(Unit)
            }
        }.launchIn(componentScope)
    }

    override fun onStop() {
        timerJob?.cancel()
    }

    fun addStateChangedListener(listener: PomodoroStateChangedListener) {
        stateChangedListeners.add(listener)
    }

    fun addStopListener(listener: TimerStopListener) {
        stopListeners.add(listener)
    }

    private fun triggerAllStopListeners() {
        stopListeners.forEach { it.onStop() }
    }

    private fun triggerAllStateChangedListeners(state: PomodoroState) {
        stateChangedListeners.forEach { it.onStateChanged(state) }
    }

    data class Args(
        val componentContext: ComponentContext,
    )

    companion object {
        fun create(
            args: Args,
            getPomodoroConfigUseCase: UseCase<PomodoroConfig, Unit>,
            timerFormatter: TimerFormatter
        ) = PomodoroTimerComponent(
            componentContext = args.componentContext,
            getPomodoroConfigUseCase = getPomodoroConfigUseCase,
            timerFormatter = timerFormatter
        )
    }
}