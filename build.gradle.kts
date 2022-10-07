import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	id("org.springframework.boot") version "3.0.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.0.14.RELEASE"
	kotlin("jvm") version "1.7.10"
	kotlin("plugin.spring") version "1.7.10"
	id("com.github.johnrengelman.shadow") version "5.1.0"
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
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.7.4")
	implementation("org.springframework.boot:spring-boot-starter-security:2.7.4")
	implementation("org.springframework.boot:spring-boot-starter-web:2.7.4")
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-dependencies
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0-rc1")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")


	testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.4")
	testImplementation("org.springframework.security:spring-security-test:5.7.3")
}
tasks.withType<ShadowJar>() {
	manifest {
		attributes["Main-Class"] = "BudzillaApplication"
	}
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
