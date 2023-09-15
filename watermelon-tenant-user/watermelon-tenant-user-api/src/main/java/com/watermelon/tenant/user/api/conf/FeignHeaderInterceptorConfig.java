package com.watermelon.tenant.user.api.conf;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

/**
 * @author byh
 * @date 2023-09-14
 * @description
 */
@Configuration
public class FeignHeaderInterceptorConfig implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!ObjectUtils.isEmpty(requestAttributes)) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletRequestAttributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    // 跳过 content-length
                    if ("content-length".equals(name)) {
                        continue;
                    }
                    if (name.contains("Content-Type") && values.contains("multipart/form-data")) {
                        values = "application/json";
                    }
                    requestTemplate.header(name, values);
                }
            }
        }
    }
}
