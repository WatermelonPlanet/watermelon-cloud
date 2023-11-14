package com.watermelon.common.core.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 对称加密工具类
 * @author byh
 * @date 2023-10-23 17:07
 * @description
 */
public class AESUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";


    /**
     * 加密
     * @param data
     * @param aesKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String data,String aesKey) throws Exception {
        //AES密钥长度必须是16、24或32字节
        SecretKey secretKey = new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec iv = new IvParameterSpec(new byte[16]); // 使用全0初始化向量
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * 解密
     * @param encryptedData
     * @param aesKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptedData, String aesKey) throws Exception {
        //AES密钥长度必须是16、24或32字节
        SecretKey secretKey = new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec iv = new IvParameterSpec(new byte[16]); // 使用全0初始化向量
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] originalData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(originalData, StandardCharsets.UTF_8);
    }

}