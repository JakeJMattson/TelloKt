group = "me.jakejmattson"
version = "1.1.0"
description = "TelloKt"

plugins {
    kotlin("jvm") version "1.4.10"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}