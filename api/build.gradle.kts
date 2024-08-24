import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "8.3.0"
    id("java")
}

group = "net.minesprawl.formy"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("libs/cumulus.jar"))
}

tasks {
    assemble {
        dependsOn("shadowJar")
    }
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.geysermc.cumulus", "net.minesprawl.formy.cumulus")
}