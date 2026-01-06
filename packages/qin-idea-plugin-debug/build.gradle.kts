import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("java")
    kotlin("jvm") version "2.3.0"
    id("org.jetbrains.intellij.platform") version "2.10.5"
}

val buildTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmm"))

group = "com.qin"
version = "0.0.1-$buildTime"

// qin-cli 编译输出目录（使用 qin.config.json 配置的 build/classes）
val qinCliClasses = file("../../build/classes")

repositories {
    gradlePluginPortal()
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    // 依赖 qin-cli 核心库（编译和运行时）
    implementation(files(qinCliClasses))
    
    implementation("com.google.code.gson:gson:2.10.1")
    intellijPlatform {
        intellijIdeaUltimate("2025.3.1")
        bundledPlugin("com.intellij.java")
    }
}

kotlin {
    // 注释掉 jvmToolchain 以避免 GraalVM 版本解析问题
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
    }
    
    // 将 qin-cli 的类打包进插件 JAR
    withType<Jar> {
        from(qinCliClasses) {
            include("com/qin/core/**")
            include("com/qin/constants/**")
            include("com/qin/types/**")
        }
    }

    intellijPlatform {
        buildSearchableOptions = false
    }
}
