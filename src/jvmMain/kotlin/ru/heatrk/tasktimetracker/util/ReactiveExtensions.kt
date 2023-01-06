package ru.heatrk.tasktimetracker.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlin.math.abs

fun tickerFlow(period: Long, initialDelay: Long = 0) = flow {
    delay(initialDelay)
    while (true) {
        emit(Unit)
        delay(period)
    }
}

fun fromToTickerFlow(
    from: Long,
    to: Long,
    period: Long,
    step: Long = 1,
    initialDelay: Long = 0
) = flow {
    check(abs(to - from) % step == 0L)

    if (from == to) {
        return@flow
    }

    var value = from
    val speed = if (from < to) step else -step

    delay(initialDelay)

    while (value != to) {
        value += speed
        emit(value)
        delay(period)
    }
}

inline fun <reified T: Any> StateFlow<*>.requireValueOfType(): T {
    val state = value

    if (state !is T) {
        throw IllegalStateException("${T::class.simpleName} StateFlow value type required!")
    }

    return state
}