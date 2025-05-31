plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("kapt") version "1.9.25"
}

group = "com.five-guys-burger"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-batch")

    // SNS 로그인을 위한 라이브러리
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // 토큰 관리를 위한 Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.4.5")

    // ORM 설정
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.vladmihalcea:hibernate-types-60:2.21.1")
    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    implementation("com.querydsl:querydsl-core:5.1.0")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("jakarta.persistence:jakarta.persistence-api")
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    // mysql 설정
    implementation("com.mysql:mysql-connector-j:9.3.0")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// QueryDSL 설정
kapt {
    keepJavacAnnotationProcessors = true
}

// QueryDSL Q클래스 생성 위치 설정
sourceSets {
    main {
        java {
            srcDirs("$buildDir/generated/source/kapt/main")
        }
    }
}
tasks.named("runKtlintCheckOverMainSourceSet") {
    mustRunAfter("kaptKotlin")
    mustRunAfter("kaptGenerateStubsKotlin")
}

tasks.named("runKtlintCheckOverTestSourceSet") {
    mustRunAfter("kaptTestKotlin")
    mustRunAfter("kaptGenerateStubsTestKotlin")
}
