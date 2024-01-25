import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}

val jimmerVersion: String by rootProject.extra
val jimmerKspVersion: String by rootProject.extra
val hutoolVersion: String by rootProject.extra
val springBootVersion: String by rootProject.extra
val lombokVersion: String by rootProject.extra
val springDataCommonsVersion: String by rootProject.extra

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {

    api("org.babyfish.jimmer:jimmer-sql-kotlin:${jimmerVersion}")
    ksp("org.babyfish.jimmer:jimmer-ksp:${jimmerKspVersion}")

    api("cn.hutool:hutool-all:${hutoolVersion}")
    api("org.babyfish.jimmer:jimmer-spring-boot-starter:${jimmerVersion}")

    api("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
    api("org.springframework.boot:spring-boot-starter-aop:${springBootVersion}")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
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
