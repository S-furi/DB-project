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

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.7")

}

application {
    // Define the main class for the application.
    mainClass.set("db_project.view.App")
}

//Defining Test information
tasks.test {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
  filter {
    includeTestsMatching("db_project.db.*")
    includeTestsMatching("db_project.view.*")
    includeTestsMatching("db_project.buildDb.*")
  }
}

javafx {
    version = "17.0.2"
    modules("javafx.controls", "javafx.fxml")
}

/* task("buildDB", JavaExec::class) { */
/*   group = "customTasks" */
/*   main = "db_project.db.BuildDb" */
/*   classpath = sourceSets["main"].runtimeClasspath */
/* } */

task<Test>("buildDb") {
  group = "customTasks"
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed", "standardOut")
  }
  filter {
    includeTestsMatching("db_project.buildDb.BuildDb.generateDB")
  }
}

task<Test>("dropDB") {
  group = "customTasks"
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed", "standardOut")
  }
  filter {
    includeTestsMatching("db_project.buildDb.BuildDb.dropDB")
  } 
}

