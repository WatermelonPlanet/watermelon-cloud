package com.watermelon.authorization.oauth2.tokenGenerator;

import cn.hutool.core.collection.CollectionUtil;
import com.watermelon.common.core.util.AESUtil;
import lombok.SneakyThrows;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

/**
 * token 生成
 *
 * @author byh
 * @description
 */
public class AecEncryptionStringKeyGenerator implements StringKeyGenerator {

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

    public AecEncryptionStringKeyGenerator(Long userId, String clientId, String clientSecret, Set<String> scopes) {
        this.userId = userId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scopes = scopes;
    }

    @SneakyThrows
    @Override
    public String generateKey() {
        long timer = Instant.now().toEpochMilli();
        String tokenContent = Optional.ofNullable(userId)
                .map(u -> u.toString().concat(SEPARATOR).concat(clientId).concat(SEPARATOR).concat(Long.toString(timer)))
                .orElse(null);
        if(!CollectionUtils.isEmpty(scopes)){
            String scopeStr = CollectionUtil.join(scopes, COMMA);
            tokenContent.concat(SEPARATOR).concat(scopeStr);
        }
        assert tokenContent != null;
        return AESUtil.encrypt(tokenContent, AEC_KYE);
    }

}
