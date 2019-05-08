package com.xbd.svc.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("svc.request")
public class RequestProperties {

    /**
     * 单次请求超过多少时间认为该请求执行缓慢(ms)
     */
    private int slowRequestTime = 1000;

}
