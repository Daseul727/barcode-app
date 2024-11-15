// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val daggerHiltVersion = "2.51"
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.5.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$daggerHiltVersion")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}