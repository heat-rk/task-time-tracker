package ru.heatrk.tasktimetracker.util.app_data

import ru.heatrk.tasktimetracker.util.ApplicationConfigConstants

class WindowsAppDataDirectoryProvider: AppDataDirectoryProvider {
    override fun providePath() = try {
        "${System.getenv("LOCALAPPDATA")}/${ApplicationConfigConstants.APP_NAME}"
    } catch (e: SecurityException) {
        null
    }
}