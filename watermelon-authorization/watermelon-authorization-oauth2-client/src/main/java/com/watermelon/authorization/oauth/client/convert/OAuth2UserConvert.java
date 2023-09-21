package com.watermelon.authorization.oauth.client.convert;

import com.watermelon.authorization.oauth.client.dto.OAuth2ThirdUserDto;
import com.watermelon.common.core.enums.AccountPlatform;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.LinkedHashMap;

/**
 * 第三方用户转换接口定义
 * @author byh
 * @date 2023-09-21
 * @description
 */
public interface OAuth2UserConvert {

    /***
     * 暂时还用不到的 后面再用
     * @return
     */
    default AccountPlatform platform() {
        return null;
    }

    /**
     * 第三方用户信息统一转换为 OAuth2ThirdUserDto
     * @param oAuth2User
     * @param userNameAttributeName 额外的属性
     * @return
     */
    Pair<OAuth2ThirdUserDto, LinkedHashMap<String,Object>> convert(OAuth2User oAuth2User, String userNameAttributeName);


}
