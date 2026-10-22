package com.paperai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 论文写作 AI 辅助系统 - 启动类
 */
@SpringBootApplication
@MapperScan("com.paperai.mapper")
public class PaperAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperAiApplication.class, args);
        System.out.println("论文写作 AI 辅助系统后端启动成功: http://localhost:8080");
    }
}
