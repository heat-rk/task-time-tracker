package ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.heatrk.tasktimetracker.domain.models.PomodoroConfig
import ru.heatrk.tasktimetracker.domain.models.PomodoroState
import ru.heatrk.tasktimetracker.domain.state_machines.PomodoroStateMachine
import ru.heatrk.tasktimetracker.domain.usecases.GetPomodoroConfigUseCase
import ru.heatrk.tasktimetracker.presentation.screens.base.Component
import ru.heatrk.tasktimetracker.util.fromToTickerFlow
import ru.heatrk.tasktimetracker.util.time_formatter.millis.MillisecondsFormatter
import java.util.concurrent.TimeUnit

class PomodoroTimerComponent(
    componentContext: ComponentContext,
    private val getPomodoroConfigUseCase: GetPomodoroConfigUseCase,
    private val timerFormatter: MillisecondsFormatter
): Component(componentContext) {

    private var timerJob: Job? = null

    private var _pomodoroConfig: PomodoroConfig? = null
    private val pomodoroConfig get() = requireNotNull(_pomodoroConfig)

    private var _pomodoroStateMachine: PomodoroStateMachine? = null
    private val pomodoroStateMachine get() = requireNotNull(_pomodoroStateMachine)

    private val _pomodoroWorkingFinishedEvents = Channel<Unit>(Channel.BUFFERED)
    val pomodoroWorkingFinishedEvents = _pomodoroWorkingFinishedEvents.receiveAsFlow()

    private val _pomodoroStateChangedEvents = Channel<PomodoroState>(Channel.BUFFERED)
    val pomodoroStateChangedEvents = _pomodoroStateChangedEvents.receiveAsFlow()

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
            _pomodoroConfig = getPomodoroConfigUseCase()
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
                            _pomodoroWorkingFinishedEvents.send(Unit)
                            startTimer()
                        }
                        PomodoroState.CHILL_LONG -> {
                            _state.update { it.copy(isChilling = true) }
                            _pomodoroWorkingFinishedEvents.send(Unit)
                            startTimer()
                        }
                        PomodoroState.WORKING -> {
                            _state.update { it.copy(isChilling = false) }
                        }
                    }

                    _pomodoroStateChangedEvents.send(state)
                }.launchIn(this)
        }
    }

    fun onIntent(intent: PomodoroTimerIntent) = componentScope.launch {
        when (intent) {
            PomodoroTimerIntent.OnTimerStart -> {
                startTimer()
            }

            PomodoroTimerIntent.OnTimerStop -> {
                stopTimer()
            }
        }
    }

    private fun startTimer() {
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

    private fun stopTimer() {
        timerJob?.cancel()
    }

    data class Args(
        val componentContext: ComponentContext
    )

    companion object {
        fun create(
            args: Args,
            getPomodoroConfigUseCase: GetPomodoroConfigUseCase,
            timerFormatter: MillisecondsFormatter
        ) = PomodoroTimerComponent(
            componentContext = args.componentContext,
            getPomodoroConfigUseCase = getPomodoroConfigUseCase,
            timerFormatter = timerFormatter
        )
    }
}