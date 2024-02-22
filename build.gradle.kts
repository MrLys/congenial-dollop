import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.0.14.RELEASE"
	kotlin("jvm") version "1.7.10"
	kotlin("plugin.spring") version "1.7.10"
	kotlin("plugin.jpa") version "1.7.10"
	idea
}

group = "com.budzilla"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}
dependencyManagement {
	imports {
		mavenBom("org.springframework.boot:spring-boot-dependencies:2.7.4")
	}
}

dependencies {

	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.2")
	implementation("org.springframework.boot:spring-boot-starter-security:3.2.2")
	implementation("org.springframework.boot:spring-boot-starter-web:3.2.2")
	implementation("org.hibernate:hibernate-core:5.6.12.Final")
	implementation("org.hibernate:hibernate-entitymanager:5.6.12.Final")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0-rc1")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	// csv
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.16.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
	implementation("com.fasterxml.jackson.core:jackson-annotations:2.16.1")
	implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
	//prometheus
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus")
	// runtime
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	runtimeOnly("org.postgresql:postgresql")
	// test
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.2")
	testImplementation("org.springframework.security:spring-security-test:6.2.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
tasks.withType<Jar> {
	archiveFileName.set("budzilla.jar")
}
