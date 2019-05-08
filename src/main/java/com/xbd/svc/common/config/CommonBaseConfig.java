package com.xbd.svc.common.config;

import com.xbd.svc.common.properties.RequestProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RequestProperties.class)
public class CommonBaseConfig {
}
