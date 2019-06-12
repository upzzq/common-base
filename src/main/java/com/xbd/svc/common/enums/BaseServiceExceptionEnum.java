package com.xbd.svc.common.enums;

import lombok.Getter;

@Getter
public enum BaseServiceExceptionEnum {

	/**
	 * 系统出错时统一对外的异常信息
	 */
	SYS_ERROR(9999, "系统繁忙中,请稍后再试"),

	/**
	 * 接口请求成功时统一对外的信息
	 */
	REQUEST_SUCCESS(200, "success"),

	/**
	 * 请求接口时,参数错误
	 */
	PARAM_ERROR(400, "请求参数错误"),

	REMOTE_SERVICE_ERROR(1200 , "客户端远程服务调用错误"),

	REMOTE_SERVICE_EXCEPTION(1201 , "远程服务业务处理异常");
	
	private Integer code;
	
	private String message;

	BaseServiceExceptionEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
	
}
