package com.xbd.svc.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("svc.switch")
public class SwitchProperties {
	
	/**
	 * swagger配置是否启用
	 * true: 启用, false: 禁用
	 */
	private boolean swaggerEnable = false;

}
