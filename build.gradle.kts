import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "ru.heatrk"
version = "1.0-SNAPSHOT"

val kotlinVersion: String by project
val kodeinVersion: String by project
val decomposeVersion: String by project
val sqliteJdbcVersion: String by project
val exposedVersion: String by project
val immutableCollectionsVersion: String by project
val essentyVersion: String by project
val coroutinesVersion: String by project

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

                implementation("org.kodein.di:kodein-di-framework-compose:$kodeinVersion")

                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")

                implementation("com.arkivanov.essenty:parcelable:$essentyVersion")

                implementation("org.xerial:sqlite-jdbc:$sqliteJdbcVersion")
                implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$immutableCollectionsVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$coroutinesVersion")
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
