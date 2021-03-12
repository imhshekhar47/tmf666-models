val tmfCode = 666

val kotlinVersion = project.properties["kotlinVersion"]
val jacksonModuleVersion = project.properties["jacksonModuleVersion"]
val javaxPersistenceVersion = project.properties["javaxPersistenceVersion"]
val okhttpVersion = project.properties["okhttpVersion"]

plugins {
    kotlin("jvm") version "1.4.21"

    id("de.undercouch.download") version "4.1.1"
    id("org.openapi.generator") version "5.0.0"

    `maven-publish`
}

group = "org.hshekhar"
version = "0.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

sourceSets.getByName("main") {
    java.srcDirs(
        "${project.buildDir}/generated/src/main/kotlin"
    )
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "${project.group}"
            artifactId = "${project.name}"
            version = "${project.version}"

            from(components["java"])
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonModuleVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonModuleVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonModuleVersion")
    implementation("javax.persistence:javax.persistence-api:${javaxPersistenceVersion}")

    implementation("com.squareup.okhttp3:okhttp:${okhttpVersion}")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}
tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.register<de.undercouch.gradle.tasks.download.Download>("download-openapi-spec") {
    this.src("https://raw.githubusercontent.com/tmforum-apis/TMF666_AccountManagement/master/TMF666-Account-v4.0.0.swagger.json")
    dest("${project.buildDir}/${tmfCode}-openapi-spec.json")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generate") {
    generatorName.set("kotlin")
    inputSpec.set("${project.buildDir}/${tmfCode}-openapi-spec.json")
    outputDir.set("${project.buildDir}/generated/")
    packageName.set("${project.group}.tmf${tmfCode}")
    configOptions.set(mapOf(
        "dateLibrary" to "java8",
        "swaggerAnnotations" to "true",
        "serviceInterface" to "true",
        "serializationLibrary" to "jackson",
        "modelMutable" to "true",
        "enumPropertyNaming" to "original",
        "collectionType" to "list"))
    globalProperties.set(mapOf(
        "modelDocs" to "false"))
    generateApiTests.set(false)
    generateApiDocumentation.set(true)
    enablePostProcessFile.set(false)
    logToStderr.set(true)
    doLast {
        val generatedApiDir = "${project.buildDir}/generated/src/main/kotlin/${project.group.toString().replace(".","/")}/tmf${tmfCode}"
        delete(
            file("$generatedApiDir/infrastructure"),
            file("$generatedApiDir/apis")
        )
    }
}

tasks.getByName("generate").dependsOn("download-openapi-spec")
tasks.getByName("compileKotlin").dependsOn("generate")