import org.gradle.api.DefaultTask
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.get
import java.io.File

abstract class BuildConfigAssembleTask: DefaultTask() {
    @get:Input
    abstract val config: Property<BuildConfig>

    private val packageName =
        "${AppConfig.group}.${AppConfig.packageName}"

    private val packagePath =
        packageName.replace(".", "/")

    private val generatedSources =
        "${project.buildDir}/generated/src/main/kotlin"

    private val buildConfigFilePath =
        "$generatedSources/$packagePath/$BUILD_CONFIG_FILE_NAME"

    private val sourceSets =
        (project as ExtensionAware).extensions.getByName("sourceSets") as SourceSetContainer

    @TaskAction
    fun generateBuildConfig() {
        val file = File(buildConfigFilePath)

        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }

        BuildConfigWriter(config.get()).writeToFile(file)
    }

    companion object {
        private const val BUILD_CONFIG_FILE_NAME = "BuildConfig.kt"
    }
}