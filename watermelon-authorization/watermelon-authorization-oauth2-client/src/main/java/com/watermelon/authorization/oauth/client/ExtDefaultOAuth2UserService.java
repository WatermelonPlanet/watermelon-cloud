package com.watermelon.authorization.oauth.client;


import com.watermelon.authorization.oauth.client.convert.OAuth2UserConvertContext;
import com.watermelon.authorization.oauth.client.dto.OAuth2ThirdUserDto;
import com.watermelon.authorization.oauth.client.exception.OAuth2UserConvertException;
import com.watermelon.authorization.oauth.client.storage.Oauth2UserStorage;
import com.watermelon.common.core.enums.AccountPlatform;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.LinkedHashMap;

/**
 * @author byh
 * @date 2023-09-21
 * @description
 */
public class ExtDefaultOAuth2UserService extends DefaultOAuth2UserService {

    public final OAuth2UserConvertContext oAuth2UserConvertContext;

    public final Oauth2UserStorage oauth2UserStorage;

    public ExtDefaultOAuth2UserService(OAuth2UserConvertContext oAuth2UserConvertContext, Oauth2UserStorage oauth2UserStorage) {
        this.oAuth2UserConvertContext = oAuth2UserConvertContext;
        this.oauth2UserStorage = oauth2UserStorage;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();
        AccountPlatform platform = this.loginPlatformConvert(userRequest.getClientRegistration().getRegistrationId());
        //将 OAuth2User 根据不同的平台 转成统一的 第三方用户了
        Pair<OAuth2ThirdUserDto, LinkedHashMap<String, Object>> oAuth2ThirdUserConvertPair = oAuth2UserConvertContext.getInstance(platform)
                .convert(oAuth2User, userNameAttributeName);
        LinkedHashMap<String, Object> userAttributes = oAuth2ThirdUserConvertPair.getValue();
        //这个地方就存数据库了
        oauth2UserStorage.save(oAuth2ThirdUserConvertPair.getKey());
        return new DefaultOAuth2User(oAuth2User.getAuthorities(), userAttributes, userNameAttributeName);
    }

    /**
     * registrationId 转换平台枚举
     * @param registrationId
     * @return
     */
    synchronized private AccountPlatform loginPlatformConvert(String registrationId) {
        return switch (registrationId) {
            case "gitee" -> AccountPlatform.GITEE;
            case "wechat" -> AccountPlatform.WECHAT;//todo Convert
            case "qq" -> AccountPlatform.QQ;//todo Convert
            default -> throw new OAuth2UserConvertException("暂不支持该客户端[" + registrationId + "]对应的第三方平台用户信息Convert");
        };
    }
}
