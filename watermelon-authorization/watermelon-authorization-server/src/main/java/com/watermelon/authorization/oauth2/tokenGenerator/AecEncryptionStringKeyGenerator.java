package com.watermelon.authorization.oauth2.tokenGenerator;

import com.watermelon.authorization.util.AESUtil;
import lombok.SneakyThrows;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

/**
 * token 生成
 * @author byh
 * @date 2023-10-23 17:42
 * @description
 */
public  class AecEncryptionStringKeyGenerator implements StringKeyGenerator {

    private  String aecKey;
    private  String clientId;
    private  String id;

    public AecEncryptionStringKeyGenerator(String aecKey, String clientId, String id) {
        this.aecKey = aecKey;
        this.clientId = clientId;
        this.id = id;
    }

    public AecEncryptionStringKeyGenerator() {
    }



    @SneakyThrows
    @Override
    public String generateKey() {
        String tokenKey = id + "::" + clientId + "::" + aecKey;
        String aesKeyString = aecKey.substring(0, 16);
        return AESUtil.encrypt(tokenKey,aesKeyString);
    }
}
