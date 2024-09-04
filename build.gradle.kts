plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "nonabili"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://jitpack.io") }
}

extra["snippetsDir"] = file("build/generated-snippets")
extra["springCloudVersion"] = "2023.0.3"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
//	implementation("org.springframework.cloud:spring-cloud-starter-config")
//	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("net.nurigo:sdk:4.3.0")

	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.3.3")
	implementation("org.springframework.boot:spring-boot-starter-mail")

	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")

	implementation("com.github.iamport:iamport-rest-client-java:0.2.23")

	implementation("org.springframework.boot:spring-boot-starter-websocket")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
}


subprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		implementation("org.springframework.boot:spring-boot-starter")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

		implementation(project(":sso-server"))
	}

	tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
	inputs.dir(project.extra["snippetsDir"]!!)
	dependsOn(tasks.test)
}
