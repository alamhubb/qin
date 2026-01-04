package com.example;

/**
 * Qin 测试项目 - 主入口
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from Qin!");

        // 测试 Gson 依赖
        var gson = new com.google.gson.Gson();
        var json = gson.toJson(new Person("张三", 25));
        System.out.println("JSON: " + json);
    }
}

record Person(String name, int age) {
}
