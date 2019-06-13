package com.xbd.svc.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xbd.svc.common.feign.support.FeignErrorDecoder;
import com.xbd.svc.common.feign.support.FeignRequestMappingHandlerMapping;
import com.xbd.svc.common.feign.support.FeignResponseEntityDecoder;
import com.xbd.svc.common.feign.support.FeignSpringFormEncoder;
import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrations;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@Slf4j
@ConditionalOnClass(Feign.class)
public class FeignConfig {

    /**
     * 设置feign日志级别
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public Logger.Level feignLoggerLevel() {
        if (log.isDebugEnabled()) {
            log.debug("开启feign日志.......");
            return Logger.Level.FULL;
        }
        return Logger.Level.NONE;
    }

    /**
     * 自定义RequestMapping加载
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public WebMvcRegistrations feignWebRegistrations() {
        return new WebMvcRegistrationsAdapter() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new FeignRequestMappingHandlerMapping();
            }
        };
    }

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }

    /**
     * 让feign的返回结果不会 null 转为 ""
     *
     * @return
     */
    @Bean
    public Decoder feignDecoder() {
        HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(feignObjectMapper());
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        //因为重写了ResponseEntityDecoder，所以用XbdResponseEntityDecoder
        return new FeignResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    /**
     * 让feign的入参不会 null 转为 ""
     *
     * @return
     */
    @Bean
    public Encoder feignEncoder() {
        HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(feignObjectMapper());
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        return new SpringEncoder(objectFactory);
    }

    /**
     * feign单独配置一个新的ObjectMapper
     *
     * @return
     */
    private ObjectMapper feignObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        return objectMapper;
    }

    /**
     * 为feign增加文件上传功能
     * @return
     */
    @Bean
    @Primary
    @Scope("prototype")
    public Encoder multipartFormEncoder() {
        return new FeignSpringFormEncoder();
    }

    }
