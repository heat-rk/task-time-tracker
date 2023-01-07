package ru.heatrk.tasktimetracker.util.app_data

import ru.heatrk.tasktimetracker.util.ApplicationConfigConstants

class LinuxAppDataDirectoryProvider: AppDataDirectoryProvider {
    override fun providePath() = try {
        "${System.getProperty("user.home")}/.${ApplicationConfigConstants.APP_NAME}"
    } catch (e: SecurityException) {
        null
    }
}