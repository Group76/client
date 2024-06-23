import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jacocoExclusions = arrayOf(
	"com/group76/client/mapping/**",
)

plugins {
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	id("jacoco")
	id("io.gitlab.arturbosch.detekt") version "1.23.6"
}

group = "com.group76"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.retry:spring-retry")
	implementation("org.springframework.boot:spring-boot-starter-web"){
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
	}
	implementation("org.springframework.boot:spring-boot-starter-undertow")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
	implementation("org.springframework.boot:spring-boot-starter-actuator") {
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
	}
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.1") {
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
		exclude(group = "org.bouncycastle", module = "bcprov-jdk18on")
		exclude(group = "commons-fileupload", module = "commons-fileupload")
	}
	implementation("io.github.openfeign:feign-okhttp:13.2.1")

	implementation("org.springframework.cloud:spring-cloud-starter:4.1.3")
	implementation("io.awspring.cloud:spring-cloud-starter-aws-parameter-store-config:2.4.4")

	implementation("software.amazon.awssdk:sns:2.25.53")
	implementation("software.amazon.awssdk:core:2.25.53")

	implementation("software.amazon.awssdk:dynamodb:2.26.7")
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")

	testImplementation("io.mockk:mockk:1.13.11")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "21"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
	enabled = false
}

tasks.withType<JacocoReport> {
	reports {
		xml.required.set(true)
	}

	afterEvaluate {
		classDirectories.setFrom(files(classDirectories.files.map {
			fileTree(it).apply {
				exclude(*jacocoExclusions)
			}
		}))
	}

	finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports{
		csv.required = true
		xml.required = true
	}
}

tasks.withType<JacocoCoverageVerification> {
	dependsOn(tasks.jacocoTestReport)

	violationRules {
		isFailOnViolation = false
		rule {
			limit {
				minimum = BigDecimal(0.8)
			}
		}
	}

	afterEvaluate {
		classDirectories.setFrom(files(classDirectories.files.map {
			fileTree(it).apply {
				exclude(*jacocoExclusions)
			}
		}))
	}
}

detekt {
	ignoreFailures = true
	buildUponDefaultConfig = true // preconfigure defaults
	allRules = false // activate all available (even unstable) rules.
}

tasks.withType<Detekt>().configureEach {
	reports {
		html.required.set(true) // observe findings in your browser with structure and code snippets
		xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
		txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
		sarif.required.set(true) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with GitHub Code Scanning
		md.required.set(true) // simple Markdown format
	}
}

tasks.withType<Detekt>().configureEach {
	jvmTarget = "1.8"
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
	jvmTarget = "1.8"
}
