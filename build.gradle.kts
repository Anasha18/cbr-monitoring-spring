plugins {
    java
    id("org.springframework.boot") version "4.0.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "1.0.0"
description = "cbr-monitoring-spring"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2025.1.1"

dependencies {

    // spring

    // web
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    // feign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    // dev
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // resilence
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")

    // lombok
    compileOnly("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // telegram
    implementation("org.telegram:telegrambots:6.9.7.1")

    // mapstruct
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
