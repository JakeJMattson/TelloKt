group = "me.jakejmattson"
version = "1.1.0"
description = "TelloKt"

plugins {
    kotlin("jvm") version "1.3.70"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}