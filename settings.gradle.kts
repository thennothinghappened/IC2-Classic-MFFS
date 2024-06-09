
pluginManagement {

    val proguardVersion = "7.5.+"

    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://maven.minecraftforge.net/")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    // Getting Proguard to work: https://github.com/Guardsquare/proguard/issues/225#issuecomment-1195015431
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.guardsquare.proguard") {
                useModule("com.guardsquare:proguard-gradle:$proguardVersion")
            }
        }
    }

    plugins {
        id("net.minecraftforge.gradle") version "6.0.+"
        id("com.guardsquare.proguard") version proguardVersion
        kotlin("jvm") version "2.0.0"
    }

}
