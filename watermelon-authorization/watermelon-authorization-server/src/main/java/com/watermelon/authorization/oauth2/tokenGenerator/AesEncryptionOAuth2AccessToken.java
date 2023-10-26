package com.watermelon.authorization.oauth2.tokenGenerator;

import cn.hutool.core.lang.Snowflake;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;

/**
 * 有时间可以搞一搞
 *
 * @author byh
 * @description
 */
public class AesEncryptionOAuth2AccessToken extends AbstractOAuth2Token {


    private String id;

    private final AesEncryptionTokenType aesEncryptionTokenType;

    private final Set<String> scopes;

    public AesEncryptionOAuth2AccessToken(String userId, AesEncryptionTokenType aesEncryptionTokenType, String tokenValue, Instant issuedAt, Instant expiresAt) {
        this(userId, aesEncryptionTokenType, tokenValue, issuedAt, expiresAt, Collections.emptySet());
    }


    public AesEncryptionOAuth2AccessToken(String id, AesEncryptionTokenType aesEncryptionTokenType, String tokenValue, Instant issuedAt, Instant expiresAt,
                                          Set<String> scopes) {
        super(tokenValue, issuedAt, expiresAt);
        Assert.notNull(aesEncryptionTokenType, "tokenType cannot be null");
        this.id = id;
        this.aesEncryptionTokenType = aesEncryptionTokenType;
        this.scopes = Collections.unmodifiableSet((scopes != null) ? scopes : Collections.emptySet());
    }


    public String getId() {
        return id;
    }

    public AesEncryptionTokenType getAesEncryptionTokenType() {
        return aesEncryptionTokenType;
    }

    public Set<String> getScopes() {
        return this.scopes;
    }


    public static final class AesEncryptionTokenType implements Serializable {

        private static final long serialVersionUID = new Snowflake().nextId();

        public static final AesEncryptionTokenType BEARER = new AesEncryptionTokenType("Bearer");

        private final String value;

        private AesEncryptionTokenType(String value) {
            Assert.hasText(value, "value cannot be empty");
            this.value = value;
        }


        public String getValue() {
            return this.value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || this.getClass() != obj.getClass()) {
                return false;
            }
            AesEncryptionTokenType that = (AesEncryptionTokenType) obj;
            return this.getValue().equalsIgnoreCase(that.getValue());
        }

        @Override
        public int hashCode() {
            return this.getValue().hashCode();
        }

    }


}
