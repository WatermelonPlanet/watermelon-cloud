package com.watermelon.authorization.defaultauth.builtin.impl;

import com.watermelon.authorization.user.core.dto.SysRegisteredClientDto;
import com.watermelon.authorization.user.core.service.SysRegisteredClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Component;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RegisteredClient 基于 mybatis
 *
 * @author byh
 * @date 2023-09-15 11:33
 * @description
 */
@Component
@RequiredArgsConstructor
public class MybatisRegisteredClientRepository implements RegisteredClientRepository {


    private static final String CLIENT_ID_NOT_EXIST_ERROR_CODE = "client not exist";

    private static final String ZONED_DATETIME_ZONE_ID = "Asia/Shanghai";

    private final SysRegisteredClientService sysRegisteredClientService;


    @Override
    public void save(RegisteredClient registeredClient) {
        SysRegisteredClientDto sysRegisteredClientDto = new SysRegisteredClientDto();
        sysRegisteredClientDto.setClientId(registeredClient.getClientId());
        sysRegisteredClientDto.setClientName(registeredClient.getClientName());
        sysRegisteredClientDto.setClientSecret(registeredClient.getClientSecret());
        if (registeredClient.getClientIdIssuedAt() != null) {
            sysRegisteredClientDto.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt().atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime());
        }
        if (registeredClient.getClientSecretExpiresAt() != null) {
            sysRegisteredClientDto.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt().atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime());
        }
        sysRegisteredClientDto.setClientAuthenticationMethods(registeredClient.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).collect(Collectors.toSet()));
        sysRegisteredClientDto.setAuthorizationGrantTypes(registeredClient.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).collect(Collectors.toSet()));
        sysRegisteredClientDto.setRedirectUris(registeredClient.getRedirectUris());
        sysRegisteredClientDto.setPostLogoutRedirectUris(registeredClient.getPostLogoutRedirectUris());
        sysRegisteredClientDto.setScopes(registeredClient.getScopes());
        sysRegisteredClientDto.setTokenSettings(registeredClient.getTokenSettings().getSettings());
        sysRegisteredClientDto.setClientSettings(registeredClient.getClientSettings().getSettings());
        sysRegisteredClientService.saveClient(sysRegisteredClientDto);
    }

    @Override
    public RegisteredClient findById(String id) {
        SysRegisteredClientDto sysRegisteredClientDetailVo = sysRegisteredClientService.getOneById(id);
        if (sysRegisteredClientDetailVo == null) {
            throw new ClientAuthorizationException(new OAuth2Error(CLIENT_ID_NOT_EXIST_ERROR_CODE,
                    "Authorization client table data id not exist: " + id, null),
                    id);
        }
        return sysRegisteredClientDetailConvert(sysRegisteredClientDetailVo);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        SysRegisteredClientDto sysRegisteredClientDto = sysRegisteredClientService.getOneByClientId(clientId);
        if (sysRegisteredClientDto == null) {
            throw new ClientAuthorizationException(new OAuth2Error(CLIENT_ID_NOT_EXIST_ERROR_CODE,
                    "Authorization client id not exist: " + clientId, null),
                    clientId);
        }
        return sysRegisteredClientDetailConvert(sysRegisteredClientDto);
    }

    /**
     * sysRegisteredClientDetailVo 转换为 RegisteredClient
     *
     * @param sysRegisteredClientDto
     * @return
     */
    private RegisteredClient sysRegisteredClientDetailConvert(SysRegisteredClientDto sysRegisteredClientDto) {
        RegisteredClient.Builder builder = RegisteredClient
                .withId(sysRegisteredClientDto.getId())
                .clientId(sysRegisteredClientDto.getClientId())
                .clientSecret(sysRegisteredClientDto.getClientSecret())
                .clientIdIssuedAt(Optional.ofNullable(sysRegisteredClientDto.getClientIdIssuedAt())
                        .map(d -> d.atZone(ZoneId.of(ZONED_DATETIME_ZONE_ID)).toInstant())
                        .orElse(null))
                .clientSecretExpiresAt(Optional.ofNullable(sysRegisteredClientDto.getClientSecretExpiresAt())
                        .map(d -> d.atZone(ZoneId.of(ZONED_DATETIME_ZONE_ID)).toInstant())
                        .orElse(null))
                .clientName(sysRegisteredClientDto.getClientName())
                .clientAuthenticationMethods(c ->
                        c.addAll(sysRegisteredClientDto.getClientAuthenticationMethods()
                                .stream().map(ClientAuthenticationMethod::new).collect(Collectors.toSet()))
                ).authorizationGrantTypes(a ->
                        a.addAll(sysRegisteredClientDto.getAuthorizationGrantTypes()
                                .stream().map(AuthorizationGrantType::new).collect(Collectors.toSet()))
                ).redirectUris(r -> r.addAll(sysRegisteredClientDto.getRedirectUris()))
                .postLogoutRedirectUris(p -> p.addAll(sysRegisteredClientDto.getPostLogoutRedirectUris()))
                .scopes(s -> s.addAll(sysRegisteredClientDto.getScopes()))
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build());// requireAuthorizationConsent(true) 不设置 授权页不会显示
//                .tokenSettings(TokenSettings.builder().build());
        //todo clientSettings和 tokenSettings 根据需要后续自行修改
//                .clientSettings(ClientSettings.withSettings(sysRegisteredClientDetailVo.getClientSettings()).build());
        return builder.build();

    }
}
