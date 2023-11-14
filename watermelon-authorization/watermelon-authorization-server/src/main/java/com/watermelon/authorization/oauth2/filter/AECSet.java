package com.watermelon.authorization.oauth2.filter;

import com.watermelon.common.core.util.AESUtil;
import net.jcip.annotations.Immutable;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author byh
 * @date 2023-11-14 11:05
 * @description
 */
@Immutable
public class AECSet implements Serializable {

    private final static String SEPARATOR = "::";
    private final static String COMMA = ",:";
    private final static String AEC_KYE = "c79112eb6e994b51a866b6481d36004b";
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 客户端id
     */
    private String clientId;
    /**
     * 客户端密钥
     */
    private String clientSecret;
    /**
     * scopes
     */
    private Set<String> scopes;


    public AECSet(String tokenValue) throws Exception {
        String token = AESUtil.decrypt(tokenValue, AEC_KYE);
        String[] tokenArray = token.split(SEPARATOR);
        if (tokenArray.length == 0) {
            throw new OAuth2AuthorizationException(new OAuth2Error("token is error!"));
        }
        this.userId = Long.parseLong(tokenArray[0]);
        this.clientId = tokenArray[1];
        this.clientSecret = tokenArray[2];
        String[] scopesArray = tokenArray[3].split(COMMA);
        this.scopes = Arrays.stream(scopesArray).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "AECSet{" +
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
        AECSet aecSet = (AECSet) o;
        return Objects.equals(userId, aecSet.userId) && Objects.equals(clientId, aecSet.clientId) && Objects.equals(clientSecret, aecSet.clientSecret) && Objects.equals(scopes, aecSet.scopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, clientId, clientSecret, scopes);
    }
}
