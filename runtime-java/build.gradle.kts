plugins {
    `java-library`
    `maven-publish`
}

group = "dev.lolomc"
version = "0.2.1"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach { options.release.set(8) }

publishing {
    publications.create<MavenPublication>("mavenJava") { from(components["java"]) }
}
