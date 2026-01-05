plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.2.1"
}

group = "com.qin"
version = "0.1.12"

// qin-cli 编译输出目录（使用 qin.config.json 配置的 build/classes）
val qinCliClasses = file("../../build/classes")

repositories {
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
        intellijIdeaUltimate("2025.1")
        bundledPlugin("com.intellij.java")
        instrumentationTools()
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "251"
            untilBuild = "253.*"
        }
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    
    // 将 qin-cli 的类打包进插件 JAR
    withType<Jar> {
        from(qinCliClasses) {
            include("com/qin/core/**")
            include("com/qin/constants/**")
            include("com/qin/types/**")
        }
    }
}
