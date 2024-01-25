import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}

val jimmerVersion: String by rootProject.extra
val openapiUiVersion: String by rootProject.extra
val h2Version: String by rootProject.extra
val mysqlConnectorJavaVersion: String by rootProject.extra
val postgresqlVersion: String by rootProject.extra
val lettuceCoreVersion: String by rootProject.extra
val caffeineVersion: String by rootProject.extra

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {

    api(project(":repository"))
    api(project(":runtime"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    ksp("org.babyfish.jimmer:jimmer-ksp:${jimmerVersion}")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    runtimeOnly("com.h2database:h2:${h2Version}")
    runtimeOnly("mysql:mysql-connector-java:${mysqlConnectorJavaVersion}")
    runtimeOnly("org.postgresql:postgresql:${postgresqlVersion}")
    runtimeOnly("io.lettuce:lettuce-core:${lettuceCoreVersion}")
    runtimeOnly("com.github.ben-manes.caffeine:caffeine:${caffeineVersion}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

kotlin {
	sourceSets.main {
		kotlin.srcDir("build/generated/ksp/main/kotlin")
	}
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
