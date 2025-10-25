plugins {
	checkstyle
	java
	jacoco
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
    id("com.github.spotbugs") version "6.0.26"
    id("org.liquibase.gradle") version "3.0.1"
    id("co.uzzu.dotenv.gradle") version "4.0.0"
}

group = "ru.job4j.devops"
version = "1.0.0"

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.3".toBigDecimal()
            }
        }

        rule {
            isEnabled = false
            element = "CLASS"
            includes = listOf("org.gradle.*")

            limit {
                counter = "LINE"
                value = "TOTALCOUNT"
                maximum = "0.3".toBigDecimal()
            }
        }
    }
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)
	implementation(libs.springWeb)
	implementation(libs.springData)
	implementation(libs.liquibaseCore)
	implementation(libs.postgresql)

	testImplementation(libs.springTest)
	testImplementation(libs.junitJupiter)
	testImplementation(libs.assertj)

    liquibaseRuntime(libs.liquibaseCore)
    liquibaseRuntime(libs.postgresql)
    liquibaseRuntime(libs.jaxb)
    liquibaseRuntime(libs.logbackCore)
    liquibaseRuntime(libs.logbackClassic)
    liquibaseRuntime(libs.picocli)
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.liquibase:liquibase-core:4.30.0")

    }
}


tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<Javadoc>(Javadoc::class.java) {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

tasks.register<Zip>("zipJavaDoc") {
    group = "documentation" // Группа, в которой будет отображаться задача
    description = "Packs the generated Javadoc into a zip archive"

    dependsOn("javadoc") // Указываем, что задача зависит от выполнения javadoc

    from("build/docs/javadoc") // Исходная папка для упаковки
    archiveFileName.set("javadoc.zip") // Имя создаваемого архива
    destinationDirectory.set(layout.buildDirectory.dir("archives")) // Директория, куда будет сохранен архив
}

tasks.register("checkJarSize") {
    group = "verification"
    description = "Checks the size of the generated JAR file."

    dependsOn("jar") // Задача зависит от сборки JAR

    doLast {
        val jarFile = file("build/libs/${project.name}-${project.version}.jar") // Путь к JAR-файлу
        if (jarFile.exists()) {
            val sizeInMB = jarFile.length() / (1024 * 1024) // Размер в мегабайтах
            if (sizeInMB > 5) {
                println("WARNING: JAR file exceeds the size limit of 5 MB. Current size: ${sizeInMB} MB")
            } else {
                println("JAR file is within the acceptable size limit. Current size: ${sizeInMB} MB")
            }
        } else {
            println("JAR file not found. Please make sure the build process completed successfully.")
        }
    }
}

tasks.register<Zip>("archiveResources") {
    group = "custom optimization"
    description = "Archives the resources folder into a ZIP file"

    val inputDir = file("src/main/resources")
    val outputDir = layout.buildDirectory.dir("archives")

    inputs.dir(inputDir) // Входные данные для инкрементальной сборки
    outputs.file(outputDir.map { it.file("resources.zip") }) // Выходной файл

    from(inputDir)
    destinationDirectory.set(outputDir)
    archiveFileName.set("resources.zip")

    doLast {
        println("Resources archived successfully at ${outputDir.get().asFile.absolutePath}")
    }
}

tasks.register("profile") {
    doFirst {
        println(env.DB_URL.value)
    }
}


tasks.spotbugsMain {
    if (System.getenv("EXTRA_TASK_ACTIVE") != null) {
        reports.create("html") {
            required = true
            outputLocation.set(layout.buildDirectory.file("reports/spotbugs/spotbugs.html"))
        }
    }
}

tasks.test {
    if (System.getenv("EXTRA_TASK_ACTIVE") != null) {
        finalizedBy(tasks.spotbugsMain)
    }
}

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "logLevel"       to "info",
            "url"            to env.DB_URL.value,
            "username"       to env.DB_USERNAME.value,
            "password"       to env.DB_PASSWORD.value,
            "classpath"      to "src/main/resources",
            "changelogFile"  to "db/changelog/db.changelog-master.xml"
        )
    }
    runList = "main"
}
