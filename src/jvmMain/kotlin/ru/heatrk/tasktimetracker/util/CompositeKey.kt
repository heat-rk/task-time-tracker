package ru.heatrk.tasktimetracker.util

class CompositeKey(
    vararg val keys: Any
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompositeKey

        if (!keys.contentEquals(other.keys)) return false

        return true
    }

    override fun hashCode(): Int {
        return keys.contentHashCode()
    }

    override fun toString(): String {
        return "CompositeKey(keys=${keys.contentToString()})"
    }
}