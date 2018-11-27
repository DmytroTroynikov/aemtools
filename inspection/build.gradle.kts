import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include
import org.gradle.kotlin.dsl.extra
import org.jetbrains.intellij.IntelliJPlugin
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.gradle.plugin.JUnitPlatformExtension

buildscript {
    val kotlinVersion: String by extra

    repositories {
        mavenCentral()
        jcenter()
        mavenLocal()
        maven {
            setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service")
        }
    }

    dependencies {
        classpath(kotlin("gradle-plugin", kotlinVersion))
    }

}

apply {
    plugin("java")
    plugin("kotlin")
    plugin("org.jetbrains.intellij")
}

plugins {
    java
    id("org.jetbrains.intellij") version "0.3.12"
}

dependencies {
    compile(project(":common"))
    compile(project(":lang"))

    testCompile(project(":test-framework"))
}

val ideaVersion: String by extra

configure<IntelliJPluginExtension> {
    version = ideaVersion
    setPlugins(
            "IntelliLang"
    )
}

