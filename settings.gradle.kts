
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://maven.minecraftforge.net/")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("net.minecraftforge.gradle") version "5.1.76" apply false
    kotlin("jvm") version "1.9.23" apply false
}
