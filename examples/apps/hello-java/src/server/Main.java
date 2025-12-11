package server;

import base.Utils;  // 使用 java-base 包
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Qin 示例 - Spring Boot HTTP 服务器
 * 使用 java-base 共享库
 */
@SpringBootApplication
@RestController
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("🚀 Qin Spring Boot 服务器已启动");
        System.out.println("   地址: http://localhost:8080");
        System.out.println("   API:  http://localhost:8080/api/time");
    }

    // ==================== API 接口 ====================

    @GetMapping("/api/time")
    public Map<String, Object> getTime() {
        return Map.of(
            "message", "后端返回",
            "time", LocalDateTime.now().toString(),
            "server", "Qin Spring Boot Server"
        );
    }

    @PostMapping("/api/greet")
    public Map<String, Object> greet(@RequestBody(required = false) GreetRequest request) {
        String name = (request != null && request.name != null) ? request.name : "世界";
        // 使用 java-base 的 Utils 类
        String greeting = Utils.greet(name);
        return Map.of(
            "message", "后端返回: " + greeting,
            "timestamp", Utils.timestamp()
        );
    }

    @GetMapping("/api/users")
    public List<Map<String, Object>> getUsers() {
        return List.of(
            Map.of("id", 1, "name", "张三", "role", "开发者"),
            Map.of("id", 2, "name", "李四", "role", "设计师"),
            Map.of("id", 3, "name", "王五", "role", "产品经理")
        );
    }

    // ==================== 静态文件服务 ====================

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index() throws IOException {
        return readStaticFile("index.html");
    }

    @GetMapping(value = "/style.css", produces = "text/css")
    public String css() throws IOException {
        return readStaticFile("style.css");
    }

    @GetMapping(value = "/main.js", produces = "application/javascript")
    public String js() throws IOException {
        return readStaticFile("main.js");
    }

    private String readStaticFile(String filename) throws IOException {
        Path devPath = Paths.get("src/client", filename);
        if (Files.exists(devPath)) {
            return Files.readString(devPath);
        }
        Path prodPath = Paths.get("dist/static", filename);
        if (Files.exists(prodPath)) {
            return Files.readString(prodPath);
        }
        return "File not found: " + filename;
    }

    static class GreetRequest {
        public String name;
    }
}
