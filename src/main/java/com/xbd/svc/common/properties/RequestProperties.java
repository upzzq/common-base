package com.xbd.svc.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = RequestProperties.XBD_SVC_REQUEST_PREFIX)
@Data
public class RequestProperties {

	public static final String XBD_SVC_REQUEST_PREFIX = "xbd.svc.request";

	/**
	 * 次请求超过多少时间认为该请求执行缓慢(ms)
	 */
	private int slowRequestTime = 1000;
}
