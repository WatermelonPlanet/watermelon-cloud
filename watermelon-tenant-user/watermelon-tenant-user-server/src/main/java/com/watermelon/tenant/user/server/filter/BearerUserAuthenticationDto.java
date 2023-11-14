

package com.watermelon.tenant.user.server.filter;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class BearerUserAuthenticationDto implements Serializable {
    /**
     * 用户id
     */
    private final Long userId;
    /**
     * 客户端id
     */
    private final String clientId;
    /**
     * 客户端密钥
     */
    private final String clientSecret;
    /**
     * scopes
     */
    private final Set<String> scopes;


    public BearerUserAuthenticationDto(Long userId, String clientId, String clientSecret, Set<String> scopes) {
        this.userId = userId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scopes = scopes;
    }

    public Long getUserId() {
        return userId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    @Override
    public String toString() {
        return "BearerUserAuthenticationDto{" +
                "userId=" + userId +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", scopes=" + scopes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BearerUserAuthenticationDto that = (BearerUserAuthenticationDto) o;
        return Objects.equals(userId, that.userId) && Objects.equals(clientId, that.clientId) && Objects.equals(clientSecret, that.clientSecret) && Objects.equals(scopes, that.scopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, clientId, clientSecret, scopes);
    }
}
