package com.watermelon.authorization.oauth2.tokenGenerator;

import com.watermelon.authorization.util.AESUtil;
import lombok.SneakyThrows;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import java.util.Map;

/**
 * token 生成
 * @author byh
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
