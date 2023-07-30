plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    /* Example Dependency */
    /* Depend on the kotlin plugin, since we want to access it in our plugin */
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")

    /* Depend on the default Gradle API's since we want to build a custom plugin */
    implementation(gradleApi())
    implementation(localGroovy())
}