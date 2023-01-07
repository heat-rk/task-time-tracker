package ru.heatrk.tasktimetracker.util

import java.util.*


object OsUtils {
    val type by lazy {
        val os = System.getProperty("os.name", "generic").lowercase(Locale.ENGLISH)
        if (os.indexOf("mac") >= 0 || os.indexOf("darwin") >= 0) {
            Type.MACOS
        } else if (os.indexOf("win") >= 0) {
            Type.WINDOWS
        } else if (os.indexOf("nux") >= 0) {
            Type.LINUX
        } else {
            throw IllegalStateException("Unsupported $os OS :(")
        }
    }

    enum class Type {
        WINDOWS, LINUX, MACOS
    }
}