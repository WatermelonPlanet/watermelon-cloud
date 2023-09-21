package com.watermelon.authorization.oauth.client.convert;


import com.watermelon.authorization.oauth.client.enums.AccountPlatform;
import com.watermelon.authorization.oauth.client.exception.OAuth2UserConvertException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author byh
 * @date 2023-09-21
 * @description
 */
public class OAuth2UserConvertContext {

    private Map<AccountPlatform, OAuth2UserConvert> oAuth2UserConvertMap;

    /**
     * 加载 OAuth2UserConvert
     * @param oAuth2UserConvertList
     */
    public OAuth2UserConvertContext(List<OAuth2UserConvert> oAuth2UserConvertList) {
        this.oAuth2UserConvertMap = oAuth2UserConvertList.stream().collect(Collectors.toMap(OAuth2UserConvert::platform, Function.identity()));
    }
    /**
     * 获取实例
     *
     * @param platform
     * @return
     */
    public OAuth2UserConvert getInstance(AccountPlatform platform) {
        if (platform == null) {
            throw new OAuth2UserConvertException("平台类型不能为空");
        }
        OAuth2UserConvert oAuth2UserConvert = oAuth2UserConvertMap.get(platform);
        if (oAuth2UserConvert == null) {
            throw new OAuth2UserConvertException("暂不支持[" + platform + "]平台类型");
        }
        return oAuth2UserConvert;
    }
}
