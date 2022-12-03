package ru.heatrk.tasktimetracker.domain.usecases

interface UseCase<R, P> {
    suspend operator fun invoke(params: P): R
}