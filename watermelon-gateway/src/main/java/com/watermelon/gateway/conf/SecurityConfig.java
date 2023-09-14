package com.watermelon.gateway.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author byh
 * @date 2023-09-14 11:33
 * @description
 */
@Configuration(proxyBeanMethods = false)
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(authorize -> authorize
                        .anyExchange().authenticated()
                )
                .oauth2Login(withDefaults())
//                .oauth2Client(withDefaults())
                .logout(withDefaults());
        return http.build();
    }



}