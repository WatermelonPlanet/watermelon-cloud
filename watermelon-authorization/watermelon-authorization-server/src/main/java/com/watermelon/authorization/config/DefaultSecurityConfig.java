package com.watermelon.authorization.config;

import com.watermelon.authorization.federation.FederatedIdentityAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfig {

	// 过滤器链
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorize ->//① 配置鉴权的
						authorize
								.requestMatchers(
										"/assets/**",
										"/webjars/**",
										"/login",
										"/oauth2/**",
										"/oauth2/token"
								).permitAll() //② 忽略鉴权的url
								.anyRequest().authenticated()//③ 排除忽略的其他url就需要鉴权了
				)
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(formLogin ->
						formLogin
								.loginPage("/login")//④ 授权服务认证页面（可以配置相对和绝对地址，前后端分离的情况下填前端的url） `UsernamePasswordAuthenticationFilter`
				)
				.oauth2Login(oauth2Login ->
						oauth2Login
								.loginPage("/login")//⑤ oauth2的认证页面（也可配置绝对地址）
								.successHandler(authenticationSuccessHandler())//⑥ 登录成功后的处理
				);

		return http.build();
	}


	private AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new FederatedIdentityAuthenticationSuccessHandler();
	}


	/**
	 * 加密解密需要
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}



	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}


	 /**
     * 跨域过滤器配置
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(configurationSource);
    }

}
