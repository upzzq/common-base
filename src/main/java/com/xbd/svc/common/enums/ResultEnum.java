package com.xbd.svc.common.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
	
	SYS_ERROR(9999, "系统繁忙中,请稍后再试"),
	REQUEST_SUCCESS(200, "success");
	
	private Integer code;
	
	private String message;

	private ResultEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
	
}
