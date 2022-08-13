import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "ru.heatrk"
version = "1.0-SNAPSHOT"

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
                val decomposeVersion = "0.8.0"
                val kodeinVersion = "7.14.0"
                val sqliteJdbcVersion = "3.39.2.0"
                val ktormVersion = "3.5.0"

                implementation(compose.desktop.currentOs)

                implementation("org.kodein.di:kodein-di-framework-compose:$kodeinVersion")

                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")

                implementation("org.xerial:sqlite-jdbc:$sqliteJdbcVersion")
                compileOnly("org.ktorm:ktorm-core:$ktormVersion")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "tasktimetracker"
            packageVersion = "1.0.0"
        }
    }
}
