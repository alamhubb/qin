import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("java")
    kotlin("jvm") version "2.3.0"
    id("org.jetbrains.intellij.platform") version "2.10.5"
}

val buildTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmm"))
val lspUiFixture = file("fixtures/lsp-ui").canonicalFile
val workspaceRoot = file("../../..").canonicalFile
val qinCommand = workspaceRoot.resolve("qin/qin.bat")

group = "com.qin"
version = "0.0.1-$buildTime"

// qin-cli compiled classes under qin/build/classes
val qinCliClasses = file("../../build/classes").canonicalFile

repositories {
    gradlePluginPortal()
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    // Compile against qin-cli core classes.
    implementation(files(qinCliClasses))

    implementation("com.google.code.gson:gson:2.10.1")

    intellijPlatform {
        intellijIdeaUltimate("2025.3.1")
        bundledPlugin("com.intellij.java")
        // The 2025.3 LSP API is packaged in product-backend.jar.
        bundledLibrary("lib/product-backend.jar")
    }
}

kotlin {
    // Use the locally installed JDK 25 but keep plugin bytecode on Java 21.
    jvmToolchain(25)
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "253"
            untilBuild = "253.*"
        }
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }

    runIde {
        jvmArgs = listOf(
            "-Dfile.encoding=UTF-8",
            "-Dconsole.encoding=UTF-8",
            "-Dsun.stdout.encoding=UTF-8",
            "-Dsun.stderr.encoding=UTF-8"
        )
        if (gradle.startParameter.taskNames.any { it == "runIdeLspFixture" || it.endsWith(":runIdeLspFixture") }) {
            args(lspUiFixture.canonicalPath)
        }
    }

    // Bundle required qin-cli classes into the plugin JAR.
    withType<Jar> {
        from(qinCliClasses) {
            include("com/qin/core/**")
            include("com/qin/constants/**")
            include("com/qin/types/**")
            include("com/qin/bsp/**")
        }
    }

    intellijPlatform {
        buildSearchableOptions = false
    }

    register<JavaExec>("lspRegistrySmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspLanguageRegistrySmokeTestMain")
        args(file("../../..").canonicalPath)
        jvmArgs(
            "-Dfile.encoding=UTF-8",
            "-Dstdout.encoding=UTF-8",
            "-Dstderr.encoding=UTF-8"
        )
    }

    register<JavaExec>("lspServerDiagnosticsSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspServerDiagnosticsSmokeTestMain")
        args(file("../../..").canonicalPath)
        jvmArgs(
            "-Dfile.encoding=UTF-8",
            "-Dstdout.encoding=UTF-8",
            "-Dstderr.encoding=UTF-8"
        )
    }

    register<JavaExec>("lspVerificationMatrixSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspVerificationMatrixSmokeTestMain")
        args(file("../../..").canonicalPath)
        jvmArgs(
            "-Dfile.encoding=UTF-8",
            "-Dstdout.encoding=UTF-8",
            "-Dstderr.encoding=UTF-8"
        )
    }

    register<JavaExec>("lspServerCommandLineSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspServerCommandLineSmokeTestMain")
        args(file("../../..").canonicalPath)
        jvmArgs(
            "-Dfile.encoding=UTF-8",
            "-Dstdout.encoding=UTF-8",
            "-Dstderr.encoding=UTF-8"
        )
    }

    register<JavaExec>("lspPluginDescriptorSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspPluginDescriptorSmokeTestMain")
        args(file("src/main/resources/META-INF/plugin.xml").canonicalPath)
        jvmArgs(
            "-Dfile.encoding=UTF-8",
            "-Dstdout.encoding=UTF-8",
            "-Dstderr.encoding=UTF-8"
        )
    }

    register<JavaExec>("lspPluginPackageSmoke") {
        dependsOn("buildPlugin")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspPluginPackageSmokeTestMain")
        jvmArgs(
            "-Dfile.encoding=UTF-8",
            "-Dstdout.encoding=UTF-8",
            "-Dstderr.encoding=UTF-8"
        )
    }

    register<JavaExec>("lspUiFixtureSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspUiFixtureSmokeTestMain")
        args(lspUiFixture.canonicalPath)
        jvmArgs(
            "-Dfile.encoding=UTF-8",
            "-Dstdout.encoding=UTF-8",
            "-Dstderr.encoding=UTF-8"
        )
    }

    register<Exec>("qinLanguageTest") {
        workingDir = workspaceRoot.resolve("qin/packages/qin-language")
        commandLine(qinCommand.canonicalPath, "language", "test")
    }

    register<Exec>("ovsLanguageTest") {
        workingDir = workspaceRoot.resolve("ovsjs/ovs-language")
        commandLine(qinCommand.canonicalPath, "language", "test")
    }

    register<Exec>("csstsLanguageTest") {
        workingDir = workspaceRoot.resolve("cssts/cssts-language")
        commandLine(qinCommand.canonicalPath, "language", "test")
    }

    register("languageProjectsTest") {
        group = "verification"
        description = "Runs qin language test for Qin, OVS, and CSSTS language projects."
        dependsOn("qinLanguageTest")
        dependsOn("ovsLanguageTest")
        dependsOn("csstsLanguageTest")
    }

    register("runIdeLspFixture") {
        group = "intellij platform"
        description = "Runs the IDE with the Qin/OVS/CSSTS LSP UI fixture project opened."
        dependsOn("runIde")
    }

    named("check") {
        dependsOn("lspRegistrySmoke")
        dependsOn("lspServerDiagnosticsSmoke")
        dependsOn("lspVerificationMatrixSmoke")
        dependsOn("lspServerCommandLineSmoke")
        dependsOn("lspPluginDescriptorSmoke")
        dependsOn("lspPluginPackageSmoke")
        dependsOn("lspUiFixtureSmoke")
        dependsOn("languageProjectsTest")
    }

    // Keep only the latest packaged plugin artifact.
    named("buildPlugin") {
        doFirst {
            val distDir = file("build/distributions")
            if (distDir.exists()) {
                distDir.listFiles()?.forEach { it.delete() }
                println("Cleaned build/distributions/")
            }
        }
    }
}

sourceSets {
    main {
        java {
            exclude("com/qin/debug/run/QinDebugProcess.java")
            exclude("com/qin/debug/run/QinDebugProgramRunner.java")
            exclude("com/qin/debug/schema/**")
        }
    }
}
