package ru.heatrk.tasktimetracker.util.app_data

import ru.heatrk.tasktimetracker.util.ApplicationConfigConstants

class MacOsAppDataDirectoryProvider: AppDataDirectoryProvider {
    override fun providePath() = try {
        "${System.getProperty("user.home")}/Library/${ApplicationConfigConstants.APP_NAME}"
    } catch (e: SecurityException) {
        null
    }
}