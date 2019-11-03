package com.xbd.svc.common.config;

import com.xbd.svc.common.properties.IdWorkerProperties;
import com.xbd.svc.common.properties.RequestProperties;
import com.xbd.svc.common.properties.SwaggerPrpoerties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RequestProperties.class, SwaggerPrpoerties.class, IdWorkerProperties.class})
public class CommonBaseConfig {

}
