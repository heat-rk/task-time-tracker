package ru.heatrk.tasktimetracker.presentation.screens.tracker.timer

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.heatrk.tasktimetracker.domain.models.PomodoroState
import ru.heatrk.tasktimetracker.domain.models.TrackedTask
import ru.heatrk.tasktimetracker.presentation.screens.base.Component
import ru.heatrk.tasktimetracker.util.tickerFlow
import ru.heatrk.tasktimetracker.util.time_formatter.millis.MillisecondsFormatter
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

class TaskTimerComponent(
    componentContext: ComponentContext,
    private val timerFormatter: MillisecondsFormatter,
    private val defaultDispatcher: CoroutineDispatcher
): Component(componentContext) {
    private var timerJob: Job? = null

    private val _timerStartEvents = Channel<Unit>(Channel.BUFFERED)
    val timerStartEvents = _timerStartEvents.receiveAsFlow()

    private val _timerStopEvents = Channel<Unit>(Channel.BUFFERED)
    val timerStopEvents = _timerStopEvents.receiveAsFlow()

    private val _timerRegisterTaskEvents = Channel<TrackedTask>(Channel.BUFFERED)
    val timerRegisterTaskEvents = _timerRegisterTaskEvents.receiveAsFlow()

    private val passedTimeInMillis = MutableStateFlow(0L).apply {
        onEach { millis ->
            _state.update { it.copy(timePassed = timerFormatter.format(millis)) }
        }.launchIn(componentScope)
    }

    private val _state: MutableStateFlow<TaskTimerViewState> =
        MutableStateFlow(TaskTimerViewState(timePassed = timerFormatter.format(passedTimeInMillis.value)))

    val state = _state.asStateFlow()

    private val intents = MutableSharedFlow<TaskTimerIntent>().apply {
        onEach { intent ->
            when (intent) {
                is TaskTimerIntent.OnPomodoroStateChanged -> {
                    setEnabled(intent.state == PomodoroState.WORKING)
                }

                is TaskTimerIntent.OnTaskNameChange -> {
                    _state.update { it.copy(taskName = intent.value) }
                }

                is TaskTimerIntent.OnTaskDescriptionChange -> {
                    _state.update { it.copy(taskDescription = intent.value) }
                }

                TaskTimerIntent.OnStartButtonClick -> {
                    startTimer()
                }

                TaskTimerIntent.OnStopButtonClick -> {
                    stopTimer()
                }

                TaskTimerIntent.OnPomodoroWorkingFinished -> {
                    stopTimer(onlyRegister = true)
                }
            }
        }.launchIn(componentScope)
    }

    fun onIntent(intent: TaskTimerIntent) = componentScope.launch {
        intents.emit(intent)
    }

    private fun setEnabled(isEnabled: Boolean) {
        _state.update { it.copy(isEnabled = isEnabled) }
    }

    private suspend fun startTimer() {
        val millisInSecond = TimeUnit.SECONDS.toMillis(1)

        timerJob = tickerFlow(
            period = millisInSecond,
            initialDelay = millisInSecond
        ).flowOn(defaultDispatcher)
            .onEach { passedTimeInMillis.value += millisInSecond }
            .launchIn(componentScope)

        _state.update { it.copy(isRunning = true) }

        _timerStartEvents.send(Unit)
    }

    private suspend fun stopTimer(onlyRegister: Boolean = false) {
        val state = state.value
        val passedTimeInMillisValue = passedTimeInMillis.value

        val task = TrackedTask(
            title = state.taskName,
            description = state.taskDescription,
            duration = Duration.ofMillis(passedTimeInMillisValue),
            startAt = LocalDateTime.ofInstant(
                Instant.now().minusMillis(passedTimeInMillisValue),
                ZoneId.systemDefault()
            )
        )

        _timerRegisterTaskEvents.send(task)

        if (!onlyRegister) {
            _timerStopEvents.send(Unit)
        }

        timerJob?.cancel()
        passedTimeInMillis.value = 0
        _state.update { it.copy(isRunning = false) }
    }

    data class Args(
        val componentContext: ComponentContext
    )

    companion object {
        fun create(
            args: Args,
            timerFormatter: MillisecondsFormatter,
            defaultDispatcher: CoroutineDispatcher
        ) = TaskTimerComponent(
            componentContext = args.componentContext,
            timerFormatter = timerFormatter,
            defaultDispatcher = defaultDispatcher
        )
    }
}