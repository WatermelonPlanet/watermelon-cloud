package com.watermelon.tenant.user.api.conf;



import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author byh
 * @date 2023-09-14
 */
@Configuration
@EnableFeignClients(basePackages = "com.watermelon.tenant.user.api.feignclients")
public class UserApiAutoConfiguration {

}
