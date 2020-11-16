<p align="center">
  <a href="https://kotlinlang.org/">
    <img src="https://img.shields.io/badge/Kotlin-1.4.10-blue.svg?logo=Kotlin" alt="Kotlin">
  </a>
  <a href="https://GitHub.com/JakeJMattson/TelloKt/releases/">
    <img src="https://img.shields.io/github/release/JakeJMattson/TelloKt.svg" alt="Release">
  </a>
  <a href="LICENSE.md">
    <img src="https://img.shields.io/github/license/JakeJMattson/TelloKt.svg" alt="License">
  </a>
</p>

# TelloKt
A Kotlin wrapper for the Tello SDK allowing you to interface with a Tello drone from a Kotlin or Java project. This library allows you to avoid the tedious work of manually sending the packets yourself and lets you focus on making the drone do what you want.

## Tello SDK
This wrapper is currently compliant with [Tello SDK 1.3.0.0](https://dl-cdn.ryzerobotics.com/downloads/tello/20180910/Tello%20SDK%20Documentation%20EN_1.3.pdf)

## Download

Maven:
```xml
<dependency>
    <groupId>com.github.jakejmattson</groupId>
    <artifactId>tellokt</artifactId>
    <version>1.1.0</version>
</dependency>
```

Gradle:
```gradle
dependencies {
  implementation 'com.github.jakejmattson:tellokt:1.1.0'
}
```

## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
