
# Qin Java Rewrite

杩欐槸 Qin 鏋勫缓宸ュ叿鐨?Java 閲嶅啓鐗堟湰銆?

## 椤圭洰缁撴瀯

```
src/java-rewrite/
鈹溾攢鈹€ com/qin/
鈹?  鈹溾攢鈹€ cli/                    # CLI 鍏ュ彛
鈹?  鈹?  鈹斺攢鈹€ QinCli.java         # 涓诲懡浠よ鍏ュ彛
鈹?  鈹溾攢鈹€ commands/               # 鍛戒护瀹炵幇
鈹?  鈹?  鈹溾攢鈹€ InitCommand.java    # init 鍛戒护
鈹?  鈹?  鈹斺攢鈹€ EnvCommand.java     # env 鍛戒护
鈹?  鈹溾攢鈹€ core/                   # 鏍稿績妯″潡
鈹?  鈹?  鈹溾攢鈹€ ConfigLoader.java   # 閰嶇疆鍔犺浇鍣?
鈹?  鈹?  鈹溾攢鈹€ DependencyResolver.java  # 渚濊禆瑙ｆ瀽鍣?
鈹?  鈹?  鈹溾攢鈹€ EnvironmentChecker.java  # 鐜妫€鏌ュ櫒
鈹?  鈹?  鈹溾攢鈹€ FatJarBuilder.java  # Fat Jar 鏋勫缓鍣?
鈹?  鈹?  鈹溾攢鈹€ JavaRunner.java     # Java 杩愯鍣?
鈹?  鈹?  鈹溾攢鈹€ PluginDetector.java # 鎻掍欢妫€娴嬪櫒
鈹?  鈹?  鈹溾攢鈹€ PluginManager.java  # 鎻掍欢绠＄悊鍣?
鈹?  鈹?  鈹溾攢鈹€ WorkspaceLoader.java # 宸ヤ綔鍖哄姞杞藉櫒
鈹?  鈹?  鈹溾攢鈹€ WorkspacePackage.java # 宸ヤ綔鍖哄寘
鈹?  鈹?  鈹斺攢鈹€ DetectionResult.java # 妫€娴嬬粨鏋?
鈹?  鈹溾攢鈹€ java/                   # Java 宸ュ叿
鈹?  鈹?  鈹溾攢鈹€ ClasspathUtils.java # Classpath 宸ュ叿
鈹?  鈹?  鈹溾攢鈹€ JavaBuilder.java    # Java 鏋勫缓鍣?
鈹?  鈹?  鈹斺攢鈹€ PackageManager.java # 鍖呯鐞嗗櫒
鈹?  鈹斺攢鈹€ types/                  # 绫诲瀷瀹氫箟
鈹?      鈹溾攢鈹€ QinConfig.java      # 閰嶇疆绫?
鈹?      鈹溾攢鈹€ QinPlugin.java      # 鎻掍欢鎺ュ彛
鈹?      鈹溾攢鈹€ PluginContext.java  # 鎻掍欢涓婁笅鏂?
鈹?      鈹溾攢鈹€ BuildResult.java    # 鏋勫缓缁撴灉
鈹?      鈹溾攢鈹€ CompileResult.java  # 缂栬瘧缁撴灉
鈹?      鈹溾攢鈹€ ResolveResult.java  # 瑙ｆ瀽缁撴灉
鈹?      鈹斺攢鈹€ ...                 # 鍏朵粬绫诲瀷
鈹斺攢鈹€ README.md
```

## 涓?TypeScript 鐗堟湰鐨勫搴斿叧绯?

| TypeScript 鏂囦欢 | Java 鏂囦欢 |
|----------------|-----------|
| src/types.ts | com/qin/types/*.java |
| src/cli.ts | com/qin/cli/QinCli.java |
| src/core/config-loader.ts | com/qin/core/ConfigLoader.java |
| src/core/dependency-resolver.ts | com/qin/core/DependencyResolver.java |
| src/core/environment.ts | com/qin/core/EnvironmentChecker.java |
| src/core/fat-jar-builder.ts | com/qin/core/FatJarBuilder.java |
| src/core/java-runner.ts | com/qin/core/JavaRunner.java |
| src/core/plugin-system.ts | com/qin/core/PluginManager.java |
| src/core/plugin-detector.ts | com/qin/core/PluginDetector.java |
| src/core/workspace-loader.ts | com/qin/core/WorkspaceLoader.java |
| src/java/package-manager.ts | com/qin/java/PackageManager.java |
| src/java/classpath.ts | com/qin/java/ClasspathUtils.java |
| src/java/builder.ts | com/qin/java/JavaBuilder.java |
| src/commands/init.ts | com/qin/commands/InitCommand.java |
| src/commands/env.ts | com/qin/commands/EnvCommand.java |

## 缂栬瘧鍜岃繍琛?

### 渚濊禆

闇€瑕?Gson 搴撶敤浜?JSON 瑙ｆ瀽锛?
- com.google.code.gson:gson:2.10.1

### 缂栬瘧

```bash
# 鍒涘缓杈撳嚭鐩綍
mkdir -p build/classes

# 涓嬭浇 Gson
curl -L -o lib/gson-2.10.1.jar https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar

# 缂栬瘧
javac -d build/classes -cp lib/gson-2.10.1.jar $(find src/java-rewrite -name "*.java")
```

### 杩愯

```bash
java -cp build/classes:lib/gson-2.10.1.jar cli.QinCli help
```

## 鍔熻兘瀵规瘮

| 鍔熻兘 | TypeScript 鐗堟湰 | Java 鐗堟湰 |
|------|----------------|-----------|
| 閰嶇疆鍔犺浇 | 鉁?c12 澶氭牸寮?| 鉁?JSON 鏍煎紡 |
| 渚濊禆瑙ｆ瀽 | 鉁?Coursier | 鉁?Coursier |
| Java 缂栬瘧 | 鉁?| 鉁?|
| Fat Jar 鏋勫缓 | 鉁?| 鉁?|
| 鐑噸杞?| 鉁?| 鈴?寰呭疄鐜?|
| 鎻掍欢绯荤粺 | 鉁?| 鉁?鍩虹瀹炵幇 |
| Monorepo | 鉁?| 鉁?|
| 鍓嶇闆嗘垚 | Qin native dev server | Qin frontend pipeline |
| GraalVM JS | 鉁?| 鈴?寰呭疄鐜?|

## 閰嶇疆鏂囦欢鏍煎紡

Java 鐗堟湰浣跨敤 `qin.config.js` 鏍煎紡锛?

```json
{
  "name": "my-app",
  "version": "1.0.0",
  "entry": "src/Main.java",
  "dependencies": {
    "org.springframework.boot:spring-boot-starter-web": "4.0.6"
  },
  "repositories": [
    "https://maven.aliyun.com/repository/public"
  ]
}
```

## 鍛戒护

```bash
qin init      # 鍒濆鍖栭」鐩?
qin run       # 缂栬瘧骞惰繍琛?
qin build     # 鏋勫缓 Fat Jar
qin dev       # 寮€鍙戞ā寮?
qin compile   # 浠呯紪璇?
qin clean     # 娓呯悊鏋勫缓
qin sync      # 鍚屾渚濊禆
qin test      # 杩愯娴嬭瘯
```

