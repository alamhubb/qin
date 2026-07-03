import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    kotlin("jvm") version "2.3.0"
    id("org.jetbrains.intellij.platform") version "2.10.5"
}

val buildTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmm"))
val lspUiFixture = file("fixtures/lsp-ui").canonicalFile
val workspaceRoot = file("../../..").canonicalFile
val qinCommand = workspaceRoot.resolve("qin/qin.bat")
val subhutiJavaSource = workspaceRoot.resolve("slime/java-slime/subhuti-java/src/main/java")
val slimeTokenSource = workspaceRoot.resolve("slime/java-slime/slime-token/src/main/java")
val localIdeaHome = providers.gradleProperty("qinLocalIdeaHome")
    .orElse(providers.environmentVariable("QIN_LOCAL_IDEA_HOME"))
val stableSmokeJvmArgs = listOf(
    "-Xmx256m",
    "-Dfile.encoding=UTF-8",
    "-Dstdout.encoding=UTF-8",
    "-Dstderr.encoding=UTF-8",
    "-XX:-UseJVMCICompiler"
)

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
    testImplementation("junit:junit:4.13.2")

    intellijPlatform {
        if (localIdeaHome.isPresent) {
            local(localIdeaHome)
        } else {
            intellijIdeaUltimate("2025.3.1")
        }
        bundledPlugin("com.intellij.java")
        // The 2025.3 LSP API is packaged in product-backend.jar.
        bundledLibrary("lib/product-backend.jar")
        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.Plugin.LSP)
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
            untilBuild = "262.*"
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
            systemProperty("qin.lsp.fixture.openFile", lspUiFixture.resolve("good.qin").canonicalPath)
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
        jvmArgs(stableSmokeJvmArgs)
    }

    register<JavaExec>("lspServerDiagnosticsSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspServerDiagnosticsSmokeTestMain")
        args(file("../../..").canonicalPath)
        jvmArgs(stableSmokeJvmArgs)
    }

    register<JavaExec>("lspQinRegistrySmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspLanguageRegistrySmokeTestMain")
        args(file("../../..").canonicalPath, "qin")
        jvmArgs(stableSmokeJvmArgs)
    }

    register<JavaExec>("lspQinServerDiagnosticsSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspServerDiagnosticsSmokeTestMain")
        args(file("../../..").canonicalPath, "qin")
        jvmArgs(stableSmokeJvmArgs)
    }

    register<JavaExec>("lspVerificationMatrixSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspVerificationMatrixSmokeTestMain")
        args(file("../../..").canonicalPath)
        jvmArgs(stableSmokeJvmArgs)
    }

    register<JavaExec>("lspServerCommandLineSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspServerCommandLineSmokeTestMain")
        args(file("../../..").canonicalPath)
        jvmArgs(stableSmokeJvmArgs)
    }

    register<JavaExec>("lspQinServerCommandLineSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspServerCommandLineSmokeTestMain")
        args(file("../../..").canonicalPath, "qin")
        jvmArgs(stableSmokeJvmArgs)
    }

    register<JavaExec>("lspLanguageCliSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspLanguageCliSmokeTestMain")
        args(file("../../..").canonicalPath)
        jvmArgs(stableSmokeJvmArgs)
    }

    register<Exec>("qinLanguageLocalDependencyBuildSmoke") {
        group = "verification"
        description = "Verifies qin language scripts build local file: dependencies before running."
        workingDir = workspaceRoot.resolve("qin")
        commandLine(qinCommand.canonicalPath, "run", "com.qin.cli.QinCliLanguageLocalDependencyBuildSmokeTestMain")
    }

    register<Exec>("qinJavaRunnerNoCompilerFallbackSmoke") {
        group = "verification"
        description = "Verifies JavaRunner exposes javac failures directly instead of using compiler fallback."
        workingDir = workspaceRoot.resolve("qin")
        commandLine(qinCommand.canonicalPath, "run", "com.qin.core.JavaRunnerNoCompilerFallbackSmokeTestMain")
    }

    register<Exec>("qinCsstsCompilerNoFallbackSmoke") {
        group = "verification"
        description = "Verifies Qin CSSTS compiler uses RuntimeStore used styles instead of atom extraction fallback."
        workingDir = workspaceRoot.resolve("qin/packages/qin-runtime-core")
        commandLine(qinCommand.canonicalPath, "run", "com.qin.runtime.core.QinCsstsCompilerNoFallbackSmokeTestMain")
    }

    register<JavaExec>("lspPluginDescriptorSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspPluginDescriptorSmokeTestMain")
        args(file("src/main/resources/META-INF/plugin.xml").canonicalPath)
        jvmArgs(stableSmokeJvmArgs)
    }

    register<JavaExec>("lspNoLocalParserSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspNoLocalParserSmokeTestMain")
        args(file(".").canonicalPath)
        jvmArgs(stableSmokeJvmArgs)
    }

    register<JavaExec>("qinToolWindowConfigTreeSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinToolWindowConfigTreeSmokeTestMain")
        jvmArgs(stableSmokeJvmArgs)
    }

    register<JavaExec>("lspWorkspaceInventorySmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspWorkspaceInventorySmokeTestMain")
        args(file("../../..").canonicalPath)
        jvmArgs(stableSmokeJvmArgs)
    }

    register<JavaExec>("lspPluginPackageSmoke") {
        dependsOn("buildPlugin")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspPluginPackageSmokeTestMain")
        jvmArgs(stableSmokeJvmArgs)
    }

    register<JavaExec>("lspUiFixtureSmoke") {
        dependsOn("classes")
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("com.qin.debug.lsp.QinLspUiFixtureSmokeTestMain")
        args(lspUiFixture.canonicalPath)
        jvmArgs(stableSmokeJvmArgs)
    }

    register<Exec>("qinLanguageTest") {
        workingDir = workspaceRoot.resolve("qin/packages/qin-language")
        commandLine(qinCommand.canonicalPath, "language", "test")
    }

    register<Exec>("qinGeneratedParserDryRun") {
        group = "verification"
        description = "Verifies QinParser Java -> TypeScript generation metadata through qin.config.js."
        workingDir = workspaceRoot.resolve("qin/packages/qin-language")
        commandLine(qinCommand.canonicalPath, "language", "generate-parser", "--dry-run")
    }

    register<Exec>("ovsLanguageTest") {
        dependsOn("csstsLanguageTest")
        workingDir = workspaceRoot.resolve("ovsjs/ovs-language")
        commandLine(qinCommand.canonicalPath, "language", "test")
    }

    register<Exec>("csstsLanguageTest") {
        workingDir = workspaceRoot.resolve("cssts/cssts-language")
        commandLine(qinCommand.canonicalPath, "language", "test")
    }

    register<Exec>("ovsCompilerTest") {
        dependsOn("csstsCompilerTest")
        workingDir = workspaceRoot.resolve("ovsjs/ovs/ovs-compiler")
        commandLine(qinCommand.canonicalPath, "language", "test")
    }

    register<Exec>("csstsCompilerTest") {
        workingDir = workspaceRoot.resolve("cssts/cssts/cssts-compiler")
        commandLine(qinCommand.canonicalPath, "language", "test")
    }

    register("languageProjectsTest") {
        group = "verification"
        description = "Runs qin language test for Qin, OVS, and CSSTS language projects."
        dependsOn("qinLanguageTest")
        dependsOn("ovsLanguageTest")
        dependsOn("csstsLanguageTest")
    }

    register("compilerProjectsTest") {
        group = "verification"
        description = "Runs qin language test for OVS and CSSTS compiler projects."
        dependsOn("ovsCompilerTest")
        dependsOn("csstsCompilerTest")
    }

    register("lspQinMatrix") {
        group = "verification"
        description = "Runs the Qin-only generated-parser, Volar LSP, and IDEA-client gate."
        dependsOn("qinGeneratedParserDryRun")
        dependsOn("qinLanguageTest")
        dependsOn("lspQinRegistrySmoke")
        dependsOn("lspQinServerCommandLineSmoke")
        dependsOn("lspQinServerDiagnosticsSmoke")
        dependsOn("lspPluginDescriptorSmoke")
        dependsOn("lspNoLocalParserSmoke")
        dependsOn("qinToolWindowConfigTreeSmoke")
    }

    register("lspUnifiedMatrix") {
        group = "verification"
        description = "Runs the unified Qin/OVS/CSSTS generated-parser, Volar LSP, and IDEA-client matrix."
        dependsOn("qinGeneratedParserDryRun")
        dependsOn("languageProjectsTest")
        dependsOn("compilerProjectsTest")
        dependsOn("lspRegistrySmoke")
        dependsOn("lspServerCommandLineSmoke")
        dependsOn("lspServerDiagnosticsSmoke")
        dependsOn("lspLanguageCliSmoke")
        dependsOn("qinLanguageLocalDependencyBuildSmoke")
        dependsOn("qinJavaRunnerNoCompilerFallbackSmoke")
        dependsOn("qinCsstsCompilerNoFallbackSmoke")
        dependsOn("lspVerificationMatrixSmoke")
        dependsOn("lspPluginDescriptorSmoke")
        dependsOn("lspNoLocalParserSmoke")
        dependsOn("qinToolWindowConfigTreeSmoke")
        dependsOn("lspWorkspaceInventorySmoke")
        dependsOn("lspPluginPackageSmoke")
        dependsOn("lspUiFixtureSmoke")
    }

    register<Exec>("qinJvmClassTargetSmoke") {
        group = "verification"
        description = "Runs the Qin CLI JVM .class smoke through qin.config.js project management."
        workingDir = workspaceRoot.resolve("qin/packages/qin-lang-cli")
        commandLine(qinCommand.canonicalPath, "run", "com.qin.lang.cli.SmokeTestMain")
    }

    register<Exec>("qinJvmClassDeclarationSmoke") {
        group = "verification"
        description = "Runs the Qin JVM class-declaration emission corpus through qin.config.js project management."
        workingDir = workspaceRoot.resolve("qin/packages/qin-lang-backend-jvm")
        commandLine(qinCommand.canonicalPath, "run", "com.qin.lang.backend.jvm.QinJvmClassDeclarationCorpusSmokeTestMain")
    }

    register("runIdeLspFixture") {
        group = "intellij platform"
        description = "Runs the IDE with the Qin/OVS/CSSTS LSP UI fixture project opened."
        dependsOn("runIde")
    }

    named("check") {
        dependsOn("lspUnifiedMatrix")
        dependsOn("qinJvmClassTargetSmoke")
        dependsOn("qinJvmClassDeclarationSmoke")
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
            srcDir(subhutiJavaSource)
            srcDir(slimeTokenSource)
            include("com/qin/**")
            include("com/subhuti/lexer/SubhutiLexer.java")
            include("com/subhuti/lexer/TokenCacheEntry.java")
            include("com/subhuti/lexer/LexerState.java")
            include("com/subhuti/struct/**")
            include("com/slime/token/**")
            exclude("com/subhuti/aop/**")
            exclude("com/subhuti/lookahead/**")
            exclude("com/subhuti/parser/**")
            exclude("com/qin/debug/run/QinDebugProcess.java")
            exclude("com/qin/debug/run/QinDebugProgramRunner.java")
            exclude("com/qin/debug/schema/**")
        }
    }
}
