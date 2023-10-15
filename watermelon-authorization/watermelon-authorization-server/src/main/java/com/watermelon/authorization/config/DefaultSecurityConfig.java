package com.watermelon.authorization.config;

import com.watermelon.authorization.consent.AuthorizationServerConfigurationConsent;
import com.watermelon.authorization.federation.FederatedIdentityAuthenticationSuccessHandler;
import com.watermelon.authorization.filter.PhoneCaptchaAuthenticationProvider;
import com.watermelon.authorization.filter.UserAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfig {


	@Autowired
	private PhoneCaptchaAuthenticationProvider phoneCaptchaAuthenticationProvider;



	// 过滤器链
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter();
		http
				.addFilterAt(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests(authorize ->//① 配置鉴权的
						authorize
								.requestMatchers(
										"/assets/**",
										"/webjars/**",
										AuthorizationServerConfigurationConsent.LOGIN_PAGE_URL,
										"/oauth2/**",
										"/oauth2/token"
								).permitAll() //② 忽略鉴权的url
								.anyRequest().authenticated()//③ 排除忽略的其他url就需要鉴权了
				)
				.csrf(AbstractHttpConfigurer::disable)
				.authenticationProvider(phoneCaptchaAuthenticationProvider)
				.formLogin(login->
						login.loginPage(AuthorizationServerConfigurationConsent.LOGIN_PAGE_URL)
								.defaultSuccessUrl("/test") // 登录成功后的跳转路径
				)
				.oauth2Login(oauth2Login->
						oauth2Login.loginPage(AuthorizationServerConfigurationConsent.LOGIN_PAGE_URL)
				);

		DefaultSecurityFilterChain build = http.build();
		userAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		userAuthenticationFilter.setSessionAuthenticationStrategy(http.getSharedObject(SessionAuthenticationStrategy.class));
		SecurityContextRepository securityContextRepository =new DelegatingSecurityContextRepository(
				new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
		userAuthenticationFilter.setSecurityContextRepository(securityContextRepository);

		return build;
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


	//如果正在使用Spring Security 的并发会话控制功能，建议注册 SessionRegistry @Bean以确保它在 Spring Security 的并发会话控制和 Spring 授权服务器的注销功能之间共享。
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
