/**
 * socialuni-common-api
 * 从 Maven pom.xml 转换而来
 * 
 * 注意：这个项目使用 Java 8 + Spring Boot 2.x（javax.servlet）
 */
import { defineConfig } from "qin";

export default defineConfig({
  name: "socialuni-common-api",
  version: "0.3.0",

  // 库项目不需要 entry，使用 qin compile 编译
  
  // Java 配置
  java: {
    version: "8",
    sourceDir: "src/main/java",
  },

  // Maven 依赖转换（Spring Boot 2.7.x 兼容 Java 8）
  dependencies: {
    // JWT
    "io.jsonwebtoken:jjwt": "0.9.1",
    
    // Swagger
    "io.swagger.core.v3:swagger-core": "2.2.2",
    
    // OpenFeign (Spring Cloud 2021.x 兼容 Spring Boot 2.7.x)
    "io.github.openfeign:feign-httpclient": "11.10",
    "org.springframework.cloud:spring-cloud-starter-openfeign": "3.1.8",
    
    // Commons
    "org.apache.commons:commons-lang3": "3.12.0",
    
    // Lombok
    "org.projectlombok:lombok": "1.18.30",
    
    // Spring Boot 2.7.x (支持 javax.servlet)
    "org.springframework.boot:spring-boot-starter-web": "2.7.18",
    "org.springframework.boot:spring-boot-starter-validation": "2.7.18",
    
    // JPA (javax.persistence)
    "javax.persistence:javax.persistence-api": "2.2",
    
    // Hutool
    "cn.hutool:hutool-all": "5.8.22",
    
    // Jackson (JSON)
    "com.fasterxml.jackson.core:jackson-databind": "2.15.3",
    
    // Servlet API (javax)
    "javax.servlet:javax.servlet-api": "4.0.1",
    
    // Annotation
    "javax.annotation:javax.annotation-api": "1.3.2",
  },

  // 输出配置
  output: {
    dir: "dist",
    jarName: "socialuni-common-api-0.3.0.jar",
  },
});
