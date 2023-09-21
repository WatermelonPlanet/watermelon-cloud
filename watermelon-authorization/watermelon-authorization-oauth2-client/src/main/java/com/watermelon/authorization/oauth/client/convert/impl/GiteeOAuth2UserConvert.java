package com.watermelon.authorization.oauth.client.convert.impl;


import com.watermelon.authorization.oauth.client.dto.OAuth2ThirdUserDto;
import com.watermelon.authorization.oauth.client.enums.AccountPlatform;
import com.watermelon.authorization.oauth.client.convert.OAuth2UserConvert;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.LinkedHashMap;
import java.util.Optional;


/**
 * @author byh
 * @date 2023-09-21
 * @description
 */
public class GiteeOAuth2UserConvert implements OAuth2UserConvert {


    private final static String AVATAR_URL = "avatar_url";

    private final static String UNIQUE_ID = "id";

    private final static String NAME = "name";

    private final static String EMAIL = "email";

    private final static String PLATFORM = "platform";



    @Override
    public AccountPlatform platform() {
        return AccountPlatform.GITEE;
    }

    @Override
    public Pair<OAuth2ThirdUserDto, LinkedHashMap<String, Object>> convert(OAuth2User oAuth2User, String userNameAttributeName) {
        String avatarUrl = Optional.ofNullable(oAuth2User.getAttribute(AVATAR_URL)).map(Object::toString).orElse(null);
        String uniqueId = Optional.ofNullable(oAuth2User.getAttribute(UNIQUE_ID)).map(Object::toString).orElse(null);
        String name = Optional.ofNullable(oAuth2User.getAttribute(NAME)).map(Object::toString).orElse(null);
        String email = Optional.ofNullable(oAuth2User.getAttribute(EMAIL)).map(Object::toString).orElse(null);
        Object nameAttributeValue = Optional.ofNullable(userNameAttributeName).map(oAuth2User::getAttribute).orElse(null);
        LinkedHashMap<String, Object> userAttributesLinkedHashMap = new LinkedHashMap<>();
        //只需要部分字段就可以 不需要全部
        userAttributesLinkedHashMap.put(UNIQUE_ID, uniqueId);
        userAttributesLinkedHashMap.put(NAME, name);
        userAttributesLinkedHashMap.put(EMAIL, email);
        userAttributesLinkedHashMap.put(AVATAR_URL, avatarUrl);
        userAttributesLinkedHashMap.put(userNameAttributeName, nameAttributeValue);
        userAttributesLinkedHashMap.put(PLATFORM, this.platform().name());
        OAuth2ThirdUserDto oAuth2ThirdUserDto = new OAuth2ThirdUserDto(uniqueId, name, AccountPlatform.GITEE, avatarUrl);
        return Pair.of(oAuth2ThirdUserDto, userAttributesLinkedHashMap);
    }
}
