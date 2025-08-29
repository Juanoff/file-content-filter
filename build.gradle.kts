plugins {
    id("application")
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "edu.juanoff"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("info.picocli:picocli:4.7.7")
}

application {
    mainClass.set("edu.juanoff.FileContentFilter")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

tasks.shadowJar {
    archiveBaseName.set("file-content-filter")
    archiveVersion.set("1.0.0")
    archiveClassifier.set("")
}

tasks.test {
    useJUnitPlatform()
}