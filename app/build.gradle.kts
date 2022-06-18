/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.3/userguide/building_java_projects.html
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:30.1.1-jre")
    
    // Used for connecting to mysql DB
    implementation("mysql:mysql-connector-java:8.0.29")

    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20090211")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
}

application {
    // Define the main class for the application.
    mainClass.set("db_project.View.App")
}

javafx {
    version = "17.0.2"
    modules("javafx.controls", "javafx.fxml")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
