package com.watermelon.tenant.user.server.config;


import com.watermelon.tenant.user.server.filter.AecAuthenticationProvider;
import com.watermelon.tenant.user.server.filter.AecBearerTokenResolver;
import com.watermelon.tenant.user.server.filter.BearerTokenAecAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

/**
 * 资源服务配置
 * @author byh
 * @date 2023-09-14
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        AecAuthenticationProvider aecAuthenticationProvider = new AecAuthenticationProvider();
        BearerTokenAecAuthenticationFilter filter = new BearerTokenAecAuthenticationFilter();
        http
                .addFilterAt(filter, BearerTokenAuthenticationFilter.class)
                .authenticationProvider(aecAuthenticationProvider)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // 下边一行是放行接口的配置，被放行的接口上不能有权限注解，e.g. @PreAuthorize，否则无效
                        .requestMatchers(
                                "/sys_user/find_one_by_phone",
                                "/sys_registered_client/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(AbstractHttpConfigurer::disable)//前后端分离 无状态 单独登录场景下禁用
                .oauth2ResourceServer(oauth2 -> oauth2
                                // 可在此处添加自定义解析设置
                                .jwt(Customizer.withDefaults())
                        // 添加未携带token和权限不足异常处理
//                        .accessDeniedHandler(SecurityUtils::exceptionHandler)
//                        .authenticationEntryPoint(SecurityUtils::exceptionHandler)
                )
        ;
        DefaultSecurityFilterChain build = http.build();
        initBearerTokenAecAuthenticationFilter(http,filter);
        return build;
    }


    private void initBearerTokenAecAuthenticationFilter(HttpSecurity http, BearerTokenAecAuthenticationFilter filter) {
        RequestAttributeSecurityContextRepository requestAttributeSecurityContextRepository = new RequestAttributeSecurityContextRepository();
        BearerTokenAuthenticationEntryPoint bearerTokenAuthenticationEntryPoint = new BearerTokenAuthenticationEntryPoint();
        filter.setAuthenticationEntryPoint(bearerTokenAuthenticationEntryPoint);
        filter.setSecurityContextRepository(requestAttributeSecurityContextRepository);
        filter.setBearerTokenResolver(new AecBearerTokenResolver());
        filter.setSecurityContextHolderStrategy(SecurityContextHolder.getContextHolderStrategy());
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        filter.setAuthenticationManagerResolver(authenticationManager);
    }




}

