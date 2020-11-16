group = "me.jakejmattson"
version = "1.2.0"

plugins {
    //Core
    kotlin("jvm") version "1.4.10"

    //Publishing
    signing
    `maven-publish`
    id("io.codearte.nexus-staging") version "0.22.0"
}

repositories {
    mavenCentral()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

publishing {
    publications {
        create<MavenPublication>(Constants.projectName) {
            from(components["kotlin"])
            artifact(sourcesJar)
            artifact(javadocJar)

            pom {
                name.set(Constants.projectName)
                description.set(Constants.projectDescription)
                url.set(Constants.projectUrl)

                developers {
                    developer {
                        id.set("JakeJMattson")
                        name.set("Jake Mattson")
                        email.set("JakeJMattson@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:ssh://github.com/JakeJMattson/TelloKt.git")
                    developerConnection.set("scm:git:ssh://git@github.com:JakeJMattson/TelloKt.git")
                    url.set(Constants.projectUrl)
                }
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
            }
            repositories {
                maven {
                    url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")

                    credentials {
                        username = project.properties["nexusUsername"] as String?
                        password = project.properties["nexusPassword"] as String?
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications[Constants.projectName])
}

nexusStaging { }

object Constants {
    const val projectName = "TelloKt"
    const val projectUrl = "https://github.com/JakeJMattson/TelloKt"
    const val projectDescription = "A Kotlin wrapper for the Tello SDK"
}