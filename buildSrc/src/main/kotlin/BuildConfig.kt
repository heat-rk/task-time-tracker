enum class BuildConfig(
    val isDebug: Boolean,
    val workingDurationSecondsDefault: Long,
    val shortChillDurationSecondsDefault: Long,
    val longChillDurationSecondsDefault: Long,
    val longChillPomodorosCount: Int
) {
    DEBUG(
        isDebug = true,
        workingDurationSecondsDefault = 15L,
        shortChillDurationSecondsDefault = 5L,
        longChillDurationSecondsDefault = 10L,
        longChillPomodorosCount = 2
    ),

    RELEASE(
        isDebug = false,
        workingDurationSecondsDefault = 1800L,
        shortChillDurationSecondsDefault = 480L,
        longChillDurationSecondsDefault = 900L,
        longChillPomodorosCount = 2
    );
}