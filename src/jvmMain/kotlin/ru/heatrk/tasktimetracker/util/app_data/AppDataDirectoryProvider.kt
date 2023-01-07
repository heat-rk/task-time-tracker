package ru.heatrk.tasktimetracker.util.app_data

import ru.heatrk.tasktimetracker.util.OsUtils
import java.nio.file.Files
import kotlin.io.path.Path

interface AppDataDirectoryProvider {
    fun providePath(): String?

    companion object {
        fun create(): AppDataDirectoryProvider {
            val provider = when (OsUtils.type) {
                OsUtils.Type.WINDOWS -> WindowsAppDataDirectoryProvider()
                OsUtils.Type.LINUX -> LinuxAppDataDirectoryProvider()
                OsUtils.Type.MACOS -> MacOsAppDataDirectoryProvider()
            }

            provider.providePath()?.let { path ->
                Files.createDirectories(Path(path))
            }

            return provider
        }
    }
}