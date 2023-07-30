import java.io.File

class BuildConfigWriter(
    private val buildConfig: BuildConfig
) {
    private val packageName =
        "${AppConfig.group}.${AppConfig.packageName}"

    private val packagePath =
        packageName.replace(".", "/")

    fun writeToFile(file: File) {
        if (!file.exists()) {
            file.createNewFile()
        }

        file.writeText("package $packageName\n\nobject BuildConfig {\n${createKotlinFields()}}")
    }

    private fun createKotlinFields() = buildString {
        appendVal(name = "IS_DEBUG", value = buildConfig.isDebug)
        appendVal(name = "WORKING_DURATION_SECONDS_DEFAULT", value = buildConfig.workingDurationSecondsDefault)
        appendVal(name = "SHORT_CHILL_SECONDS_DEFAULT", value = buildConfig.shortChillDurationSecondsDefault)
        appendVal(name = "LONG_CHILL_SECONDS_DEFAULT", value = buildConfig.longChillDurationSecondsDefault)
        appendVal(name = "LONG_CHILL_POMODOROS_COUNT", value = buildConfig.longChillPomodorosCount)
    }

    private fun StringBuilder.appendVal(name: String, value: Any) {
        append("\tconst val $name = $value\n")
    }
}