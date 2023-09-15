package com.watermelon.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author byh
 * @date 2023-09-14 9:13
 * @description
 */
@SpringBootApplication
@ComponentScan("com.watermelon.**")
public class WatermelonAuthorizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(WatermelonAuthorizationApplication.class, args);
    }
}
