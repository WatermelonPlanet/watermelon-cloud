package com.watermelon.authorization.defaultauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author byh
 * @date 2023-10-30 17:48
 * @description
 */
@Configuration
public class DefaultBeanConfig {
    /**
     * 加密解密需要
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
