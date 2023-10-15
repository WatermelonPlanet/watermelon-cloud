package com.watermelon.authorization.web;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author byh
 * @date 2023-09-14 10:06
 * @description
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        return "test";
    }
}
