export default {
  name: "com.qin:qin-idea-plugin-debug",
  version: "0.0.1",
  type: "tooling",
  description: "Shared IntelliJ IDEA LSP client for Qin, OVS, and CSSTS",
  scripts: {
    check: ".\\gradlew.bat --no-daemon \"-Dorg.gradle.jvmargs=-Xmx512m -Dfile.encoding=UTF-8\" check",
    lspQinMatrix: ".\\gradlew.bat --no-daemon \"-Dorg.gradle.jvmargs=-Xmx512m -Dfile.encoding=UTF-8\" lspQinMatrix",
    lspUnifiedMatrix: ".\\gradlew.bat --no-daemon \"-Dorg.gradle.jvmargs=-Xmx512m -Dfile.encoding=UTF-8\" lspUnifiedMatrix",
    lspVerificationMatrixSmoke: ".\\gradlew.bat --no-daemon \"-Dorg.gradle.jvmargs=-Xmx512m -Dfile.encoding=UTF-8\" lspVerificationMatrixSmoke",
    runIdeLspFixture: ".\\gradlew.bat --no-daemon \"-Dorg.gradle.jvmargs=-Xmx512m -Dfile.encoding=UTF-8\" runIdeLspFixture",
    buildPlugin: ".\\gradlew.bat --no-daemon \"-Dorg.gradle.jvmargs=-Xmx512m -Dfile.encoding=UTF-8\" buildPlugin"
  },
  ideaPlugin: {
    buildTool: "gradle",
    platform: "intellij",
    boundary: "IntelliJ Platform plugin packaging remains Gradle-managed; Qin owns the project manifest and script entrypoints."
  }
}
