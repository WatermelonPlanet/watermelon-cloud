package com.watermelon.authorization.oauth.client.dto;



import com.watermelon.common.core.enums.AccountPlatform;

import java.io.Serializable;
import java.util.Objects;

/**
 * 第三方服务账号详情dto
 * @author byh
 * @date 2023-09-21
 * @description
 */
public class OAuth2ThirdUserDto implements Serializable {
    /**
     * 第三方平台唯一id
     */
    private String uniqueId;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 平台类型(WX:微信；QQ:QQ)
     */
    private AccountPlatform platform;
    /**
     * 头像
     */
    private String avatar;


    public OAuth2ThirdUserDto(String uniqueId, String name, AccountPlatform platform, String avatar) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.platform = platform;
        this.avatar = avatar;
    }


    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(AccountPlatform platform) {
        this.platform = platform;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OAuth2ThirdUserDto that = (OAuth2ThirdUserDto) o;
        return Objects.equals(uniqueId, that.uniqueId) && Objects.equals(name, that.name) && platform == that.platform && Objects.equals(avatar, that.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId, name, platform, avatar);
    }
}
