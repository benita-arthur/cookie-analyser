plugins {
    id("java")
}

group = "org.quantcast"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}

dependencies {
    // CLI Parsing (Allowed library per requirements)
    implementation("info.picocli:picocli:4.7.6")

    // Testing Frameworks
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Fluent Assertions for clean test logic
    testImplementation("org.assertj:assertj-core:3.25.3")
}


tasks.test {
    useJUnitPlatform()
}


tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.quantcast.cookie.Main"
    }

    // Include all runtime dependencies inside the JAR
    from({
        configurations.runtimeClasspath.get().map { file ->
            if (file.isDirectory) file else zipTree(file)
        }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
