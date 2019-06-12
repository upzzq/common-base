package com.xbd.svc.common.feign.support;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 1.设置feign中的RequestMapping不能被SpringMvc加载
 * 2.不能被@FeignClient注解修饰的类才会进行解析RequestMapping
 */
public class FeignRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return super.isHandler(beanType) &&
                !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class);
    }
}
