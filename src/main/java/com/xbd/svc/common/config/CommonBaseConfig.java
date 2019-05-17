package com.xbd.svc.common.config;

import com.xbd.svc.common.properties.RequestProperties;
import com.xbd.svc.common.properties.SwitchProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RequestProperties.class, SwitchProperties.class})
public class CommonBaseConfig {
}
