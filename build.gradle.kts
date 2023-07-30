import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = AppConfig.group
version = AppConfig.version

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(Dependencies.kodein)
                implementation(Dependencies.decompose)
                implementation(Dependencies.decomposeExtensions)
                implementation(Dependencies.sqliteJdbc)
                implementation(Dependencies.exposedCore)
                implementation(Dependencies.exposedDao)
                implementation(Dependencies.exposedJdbc)
                implementation(Dependencies.immutableCollections)
                implementation(Dependencies.coroutinesSwing)
            }

            kotlin.srcDir("${buildDir}/generated/src/main/kotlin")
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = AppConfig.mainClass
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = AppConfig.packageName
            packageVersion = AppConfig.version
        }
    }
}

tasks.register<BuildConfigAssembleTask>("assembleDebugBuildConfig") {
    config.set(BuildConfig.DEBUG)
}

tasks.register<BuildConfigAssembleTask>("assembleReleaseBuildConfig") {
    config.set(BuildConfig.RELEASE)
}

tasks.register("buildDebug") {
    dependsOn("assembleDebugBuildConfig")
    finalizedBy("run")
}

tasks.register("buildRelease") {
    dependsOn("assembleReleaseBuildConfig")
    finalizedBy("run")
}
