
package com.watermelon.authorization.oauth2.config;


import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.watermelon.authorization.SmsCodeService;
import com.watermelon.authorization.consent.AuthorizationServerConfigurationConsent;
import com.watermelon.authorization.oauth2.federation.FederatedIdentityIdTokenCustomizer;
import com.watermelon.authorization.oauth2.filter.AecEncryptionEndpointFilter;
import com.watermelon.authorization.oauth2.support.device.DeviceClientAuthenticationConverter;
import com.watermelon.authorization.oauth2.support.device.DeviceClientAuthenticationProvider;
import com.watermelon.authorization.oauth2.support.sms.SmsAuthenticationConverter;
import com.watermelon.authorization.oauth2.support.sms.SmsAuthenticationProvider;
import com.watermelon.authorization.oauth2.tokenGenerator.AesEncryptionOAuth2TokenCustomizer;
import com.watermelon.authorization.oauth2.tokenGenerator.AesEncryptionOAuth2TokenGenerator;
import com.watermelon.authorization.util.JwtKeyUtil;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.oauth2.server.authorization.web.NimbusJwkSetEndpointFilter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

    @Resource
    public SmsCodeService smsCodeService;

    @Resource
    public UserDetailsService userDetailsService;


    private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";//这个是授权页

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http, RegisteredClientRepository registeredClientRepository,
            AuthorizationServerSettings authorizationServerSettings) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        DeviceClientAuthenticationConverter deviceClientAuthenticationConverter =
                new DeviceClientAuthenticationConverter(
                        authorizationServerSettings.getDeviceAuthorizationEndpoint());
        DeviceClientAuthenticationProvider deviceClientAuthenticationProvider =
                new DeviceClientAuthenticationProvider(registeredClientRepository);
        // @formatter:off
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .deviceAuthorizationEndpoint(deviceAuthorizationEndpoint ->
                        deviceAuthorizationEndpoint.verificationUri("/activate")
                )
                .deviceVerificationEndpoint(deviceVerificationEndpoint ->
                        deviceVerificationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI)
                )
                .clientAuthentication(clientAuthentication ->
                        clientAuthentication
                                .authenticationConverter(deviceClientAuthenticationConverter)
                                .authenticationProvider(deviceClientAuthenticationProvider)
                )
                .authorizationEndpoint(authorizationEndpoint ->
                        authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI))
                .oidc(Customizer.withDefaults());    // Enable OpenID Connect 1.0
        // @formatter:on

        // @formatter:off
        http
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint(AuthorizationServerConfigurationConsent.LOGIN_PAGE_URL),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(Customizer.withDefaults()));
        // @formatter:on

        //sms off
        SmsAuthenticationConverter smsAuthenticationConverter = new SmsAuthenticationConverter();
        SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider();
        //sms on
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(tokenEndpoint->
                        tokenEndpoint.accessTokenRequestConverter(smsAuthenticationConverter)
                                     .authenticationProvider(smsAuthenticationProvider)//选择追加的方式
                );

        //AecEncryptionEndpointFilter
        http.addFilterBefore(new AecEncryptionEndpointFilter(), AbstractPreAuthenticatedProcessingFilter.class);

        DefaultSecurityFilterChain build = http.build();
        this.initAuthenticationProviderFiled(http, smsAuthenticationProvider);
        return build;
    }



    //todo 授权信息也可存储redis中
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> idTokenCustomizer() {
        return new FederatedIdentityIdTokenCustomizer();
    }


    @Bean
    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        RSAKey rsaKey = JwtKeyUtil.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        AuthorizationServerSettings build = AuthorizationServerSettings
                .builder()
                .jwkSetEndpoint("/oauth2/aecs").build();
        return build;
    }


    /**
     * 初始化  Provider 中的 OAuth2TokenGenerator、AuthenticationManager、OAuth2AuthorizationService 属性
     * @param http
     * @param providers
     */
    private void initAuthenticationProviderFiled(HttpSecurity http, AuthenticationProvider... providers) {
        //http.build 之后 Spring Security过滤器链才完整构建 这个时候才能从中获取到以下想要获取到的class实例（其他方法后面有时间再试一试）
        OAuth2TokenGenerator<?> tokenGenerator = http.getSharedObject(OAuth2TokenGenerator.class);
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);
        for (AuthenticationProvider provider : providers) {
            if (provider instanceof SmsAuthenticationProvider smsAuthenticationProvider) {
                //这个class需要用到依赖
                smsAuthenticationProvider.setAuthorizationService(authorizationService);
                smsAuthenticationProvider.setTokenGenerator(tokenGenerator);
                smsAuthenticationProvider.setAuthenticationManager(authenticationManager);
                smsAuthenticationProvider.setSmsCodeService(smsCodeService);
                smsAuthenticationProvider.setUserDetailsService(userDetailsService);
            }
        }
    }


    /**
     * 替换jwt的token生成
     */
    @Bean
    public OAuth2TokenGenerator oAuth2TokenGenerator() {
        AesEncryptionOAuth2TokenGenerator aesEncryptionOAuth2TokenGenerator = new AesEncryptionOAuth2TokenGenerator();
        return new DelegatingOAuth2TokenGenerator(aesEncryptionOAuth2TokenGenerator, new OAuth2RefreshTokenGenerator());
    }
}

