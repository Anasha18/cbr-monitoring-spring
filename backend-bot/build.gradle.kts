plugins {
    java
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "backend-bot"

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

    // data jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // liquibase
    implementation("org.springframework.boot:spring-boot-starter-liquibase")

    // validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // mvc
    implementation("org.springframework.boot:spring-boot-starter-webmvc")

    // cache
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // resilence
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")

    // feign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // dev tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // postgresql
    runtimeOnly("org.postgresql:postgresql")

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
