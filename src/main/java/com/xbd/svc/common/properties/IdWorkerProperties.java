package com.xbd.svc.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("xbd.worker")
@Data
public class IdWorkerProperties {

    /**
     * 当前机器Id
     */
    private long workerId;

    /**
     * 数据中心id
     */
    private long datacenterId;
}
