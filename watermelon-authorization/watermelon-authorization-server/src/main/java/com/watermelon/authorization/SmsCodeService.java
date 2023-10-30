package com.watermelon.authorization;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author byh
 * @date 2023-10-25 15:54
 * @description
 */
@Slf4j
@Service
public class SmsCodeService {


    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Value(value = "${phone.valid-code.expiration-time}")
    private Long validCodeExpirationTime;

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    public String sentValidCode(String phone) {
        //生成验证码
        String sentValidateCode = RandomUtil.randomNumbers(6);
        //构建验证码key
        String key = SmsCodeKeyBuild.buildPhoneValidCodeKey(phone);
        //存储在redis
        redisTemplate.setValueSerializer(StringRedisSerializer.UTF_8);
        redisTemplate.opsForValue().set(key, sentValidateCode, validCodeExpirationTime, TimeUnit.SECONDS);
        //todo 短信发送
        log.info("sentValidCode success [phone:{},code:{}]", phone, sentValidateCode);
        return sentValidateCode;
    }

    /**
     * 验证码验证
     * @param phone
     * @param code
     * @return
     */
    public boolean checkValidCode(String phone, String code) {
        String key = SmsCodeKeyBuild.buildPhoneValidCodeKey(phone);
        String sentValidateCode = (String) redisTemplate.opsForValue().get(key);
        return code.equals(sentValidateCode);
    }



    public static class SmsCodeKeyBuild{
        public static String buildPhoneValidCodeKey(String phone){
            return "PHONE_VALIDATE_CODE_KEY::" + phone;
        }
    }
}
