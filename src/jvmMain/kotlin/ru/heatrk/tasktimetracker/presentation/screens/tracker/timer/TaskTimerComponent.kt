package ru.heatrk.tasktimetracker.presentation.screens.tracker.timer

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.heatrk.tasktimetracker.domain.models.PomodoroState
import ru.heatrk.tasktimetracker.domain.models.TrackedTask
import ru.heatrk.tasktimetracker.presentation.screens.base.Component
import ru.heatrk.tasktimetracker.presentation.screens.tracker.TaskStopListener
import ru.heatrk.tasktimetracker.presentation.screens.tracker.TimerStartListener
import ru.heatrk.tasktimetracker.presentation.screens.tracker.TimerStopListener
import ru.heatrk.tasktimetracker.presentation.screens.tracker.pomodoro.PomodoroStateChangedListener
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
): Component(componentContext), TimerStopListener, PomodoroStateChangedListener {
    private var timerJob: Job? = null

    private val startListeners = mutableSetOf<TimerStartListener>()
    private val stopListeners = mutableSetOf<TimerStopListener>()
    private val taskStopListeners = mutableListOf<TaskStopListener>()

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
            }
        }.launchIn(componentScope)
    }

    override fun onStateChanged(state: PomodoroState) {
        setEnabled(state == PomodoroState.WORKING)
    }

    override fun onStop() {
        stopTimer()
    }

    fun onIntent(intent: TaskTimerIntent) = componentScope.launch {
        intents.emit(intent)
    }

    fun addStartListener(listener: TimerStartListener) {
        startListeners.add(listener)
    }

    fun addStopListener(listener: TimerStopListener) {
        stopListeners.add(listener)
    }

    fun addTaskStopListener(listener: TaskStopListener) {
        taskStopListeners.add(listener)
    }

    private fun setEnabled(isEnabled: Boolean) {
        _state.update { it.copy(isEnabled = isEnabled) }
    }

    private fun startTimer() {
        val millisInSecond = TimeUnit.SECONDS.toMillis(1)

        timerJob = tickerFlow(
            period = millisInSecond,
            initialDelay = millisInSecond
        ).flowOn(defaultDispatcher)
            .onEach { passedTimeInMillis.value += millisInSecond }
            .launchIn(componentScope)

        _state.update { it.copy(isRunning = true) }

        triggerAllStartListeners()
    }

    private fun stopTimer() {
        triggerAllTaskStopListeners()
        timerJob?.cancel()
        passedTimeInMillis.value = 0
        _state.update { it.copy(isRunning = false) }
        triggerAllStopListeners()
    }

    private fun triggerAllStartListeners() {
        startListeners.forEach { it.onStart() }
    }

    private fun triggerAllStopListeners() {
        stopListeners.forEach { it.onStop() }
    }

    private fun triggerAllTaskStopListeners() {
        val state = state.value
        val passedTimeInMillis = passedTimeInMillis.value

        val task = TrackedTask(
            title = state.taskName,
            description = state.taskDescription,
            duration = Duration.ofMillis(passedTimeInMillis),
            startAt = LocalDateTime.ofInstant(
                Instant.now().minusMillis(passedTimeInMillis),
                ZoneId.systemDefault()
            )
        )

        taskStopListeners.forEach { it.onStop(task) }
    }

    override fun onDestroy() {
        startListeners.clear()
        stopListeners.clear()
        taskStopListeners.clear()
        super.onDestroy()
    }

    data class Args(
        val componentContext: ComponentContext,
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