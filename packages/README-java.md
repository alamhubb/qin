# Qin Packages - Java 瀹炵幇

杩欐槸 Qin 鍚勪釜鎻掍欢鍖呯殑 Java 閲嶅啓鐗堟湰銆?

## 缂栬瘧鐘舵€?

鉁?鎵€鏈?Java 鏂囦欢宸查€氳繃缂栬瘧楠岃瘉锛圝ava 25锛?

## 鍖呯粨鏋?

```
packages/
鈹溾攢鈹€ create-qin/src/java/
鈹?  鈹斺攢鈹€ com/qin/create/
鈹?      鈹斺攢鈹€ CreateQin.java          # 椤圭洰鑴氭墜鏋跺伐鍏?
鈹?
鈹溾攢鈹€ qin-plugin-java/src/java/
鈹?  鈹斺攢鈹€ com/qin/plugins/
鈹?      鈹溾攢鈹€ JavaPlugin.java         # Java 璇█鏀寔鎻掍欢
鈹?      鈹斺攢鈹€ PluginInterfaces.java   # 鎻掍欢鎺ュ彛瀹氫箟
鈹?
鈹溾攢鈹€ qin-plugin-java-hot-reload/src/java/
鈹?  鈹斺攢鈹€ com/qin/plugins/
鈹?      鈹斺攢鈹€ HotReloadPlugin.java    # Java 鐑噸杞芥彃浠?
鈹?
鈹溾攢鈹€ qin-plugin-spring/src/java/
鈹?  鈹斺攢鈹€ com/qin/plugins/
鈹?      鈹斺攢鈹€ SpringPlugin.java       # Spring Boot 鏀寔鎻掍欢
鈹?
鈹溾攢鈹€ qin-plugin-vite/src/java/
鈹?  鈹斺攢鈹€ com/qin/plugins/
鈹?      鈹斺攢鈹€ VitePlugin.java         # Vite 鍓嶇闆嗘垚鎻掍欢
鈹?
鈹溾攢鈹€ qin-plugin-graalvm/src/java/
鈹?  鈹斺攢鈹€ com/qin/plugins/
鈹?      鈹斺攢鈹€ GraalVMPlugin.java      # GraalVM 杩愯鏃舵敮鎸?
鈹?
鈹斺攢鈹€ qin-plugin-graalvm-js/src/java/
    鈹斺攢鈹€ com/qin/plugins/
        鈹斺攢鈹€ GraalVMJsPlugin.java    # GraalVM JavaScript 鏀寔
```

## 涓?TypeScript 鐗堟湰鐨勫搴斿叧绯?

| TypeScript 鍖?| Java 绫?| 鍔熻兘 |
|--------------|---------|------|
| create-qin | CreateQin | 浜や簰寮忛」鐩垱寤?|
| qin-plugin-java | JavaPlugin | Java 缂栬瘧/杩愯/鏋勫缓 |
| qin-plugin-java-hot-reload | HotReloadPlugin | 鏂囦欢鐩戝惉鍜岀儹閲嶈浇 |
| qin-plugin-spring | SpringPlugin | Spring Boot 閰嶇疆鐢熸垚 |
| qin-plugin-vite | VitePlugin | Vite 寮€鍙戞湇鍔″櫒闆嗘垚 |
| qin-plugin-graalvm | GraalVMPlugin | GraalVM 鐜妫€娴?|
| qin-plugin-graalvm-js | GraalVMJsPlugin | GraalVM JS 鎵ц |

## 鍔熻兘瀵规瘮

### create-qin

| 鍔熻兘 | TypeScript | Java |
|------|------------|------|
| 浜や簰寮忓垱寤?| 鉁?| 鉁?|
| 澶氳瑷€鏀寔 | 鉁?Java/Bun/Node | 鉁?Java/Bun/Node |
| 澶氭ā鏉挎敮鎸?| 鉁?fullstack/monorepo | 鉁?fullstack/monorepo |
| 鍛戒护琛屽弬鏁?| 鉁?| 鉁?|

### qin-plugin-java

| 鍔熻兘 | TypeScript | Java |
|------|------------|------|
| Java 缂栬瘧 | 鉁?| 鉁?|
| 杩愯绋嬪簭 | 鉁?| 鉁?|
| Fat JAR 鏋勫缓 | 鉁?| 鉁?|
| JUnit 娴嬭瘯 | 鉁?| 鉁?鍩虹瀹炵幇 |
| 璧勬簮鏂囦欢澶勭悊 | 鉁?| 鉁?|
| 鐑噸杞介泦鎴?| 鉁?| 鉁?閫氳繃鎻掍欢缁勫悎 |

### qin-plugin-java-hot-reload

| 鍔熻兘 | TypeScript | Java |
|------|------------|------|
| 鏂囦欢鐩戝惉 | 鉁?chokidar | 鉁?WatchService |
| 闃叉姈澶勭悊 | 鉁?| 鉁?|
| 鑷姩閲嶇紪璇?| 鉁?| 鉁?|
| DevTools 妫€娴?| 鉁?| 鉁?|

### qin-plugin-spring

| 鍔熻兘 | TypeScript | Java |
|------|------------|------|
| Spring Boot 妫€娴?| 鉁?| 鉁?|
| DevTools 妫€娴?| 鉁?| 鉁?|
| application.yml 鐢熸垚 | 鉁?| 鉁?|
| 閰嶇疆杞崲 | 鉁?camelCase鈫択ebab | 鉁?|

### qin-plugin-vite

| 鍔熻兘 | TypeScript | Java |
|------|------------|------|
| 鍓嶇鐩綍妫€娴?| 鉁?| 鉁?|
| Vite 閰嶇疆鐢熸垚 | 鉁?| 鉁?|
| 寮€鍙戞湇鍔″櫒鍚姩 | 鉁?| 鉁?|
| API 浠ｇ悊閰嶇疆 | 鉁?| 鉁?|
| 鐢熶骇鏋勫缓 | 鉁?| 鉁?|

### qin-plugin-graalvm

| 鍔熻兘 | TypeScript | Java |
|------|------------|------|
| GraalVM 妫€娴?| 鉁?| 鉁?|
| 缁勪欢鍒楄〃 | 鉁?| 鉁?|
| 鐗堟湰淇℃伅 | 鉁?| 鉁?|
| 瀹夎鎸囧崡 | 鉁?| 鉁?|

### qin-plugin-graalvm-js

| 鍔熻兘 | TypeScript | Java |
|------|------------|------|
| JS 鎵ц | 鉁?| 鉁?|
| Java 浜掓搷浣?| 鉁?--polyglot --jvm | 鉁?|
| 璇硶楠岃瘉 | 鉁?| 鉁?|
| 鐑噸杞?| 鉁?| 鉁?|
| 閿欒鏍煎紡鍖?| 鉁?| 鉁?鍩虹瀹炵幇 |

## 缂栬瘧

### 蹇€熺紪璇戯紙鎺ㄨ崘锛?

浣跨敤椤圭洰鏍圭洰褰曠殑缂栬瘧鑴氭湰锛?

```bash
# Windows
build-java.bat

# Linux/macOS
chmod +x build-java.sh
./build-java.sh
```

### 鎵嬪姩缂栬瘧

姣忎釜鍖呭彲浠ョ嫭绔嬬紪璇戯細

```bash
# 缂栬瘧 create-qin
cd packages/create-qin
javac -d build/classes src/java/com/qin/create/*.java

# 缂栬瘧 qin-plugin-java
cd packages/qin-plugin-java
javac -d build/classes src/java/com/qin/plugins/*.java

# 缂栬瘧鎵€鏈夋彃浠讹紙闇€瑕佸厛缂栬瘧鎺ュ彛瀹氫箟锛?
```

## 浣跨敤绀轰緥

### 鍒涘缓椤圭洰

```java
// 浣跨敤 CreateQin
CreateQin.main(new String[]{"my-app", "-java", "-t", "fullstack"});
```

### 浣跨敤鎻掍欢

```java
// 鍒涘缓 Java 鎻掍欢
JavaPlugin javaPlugin = JavaPlugin.create();

// 鍒涘缓 Spring 鎻掍欢
SpringBootPluginOptions springOptions = new SpringBootPluginOptions();
ServerConfig server = new ServerConfig();
server.setPort(8080);
springOptions.setServer(server);
SpringPlugin springPlugin = SpringPlugin.create(springOptions);

// 鍒涘缓 Vite 鎻掍欢
VitePluginOptions viteOptions = new VitePluginOptions();
viteOptions.setPort(5173);
VitePlugin vitePlugin = VitePlugin.create(viteOptions);
```

## 娉ㄦ剰浜嬮」

1. Java 鐗堟湰浣跨敤 `WatchService` 鏇夸唬 `chokidar` 杩涜鏂囦欢鐩戝惉
2. 閰嶇疆鏂囦欢鏍煎紡涓?JSON锛坄qin.config.js`锛夎€岄潪 TypeScript
3. 閮ㄥ垎楂樼骇鍔熻兘锛堝瀹屾暣鐨?JUnit 闆嗘垚锛変粛鍦ㄥ紑鍙戜腑
4. GraalVM 鐩稿叧鍔熻兘闇€瑕佸畨瑁?GraalVM 杩愯鏃?

