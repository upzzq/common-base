package com.xbd.svc.common.config;

import com.xbd.svc.common.properties.IdWorkerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneratorConfig {

    @Autowired
    private IdWorkerProperties idWorkerProperties;

    /*@Bean
    public SnowflakeIdWorkerGenerator snowflakeIdWorkerGenerator () {
        return new SnowflakeIdWorkerGenerator(idWorkerProperties.getWorkerId(), idWorkerProperties.getDatacenterId());
    }*/
}
