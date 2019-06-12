package com.xbd.svc.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xbd.svc.common.enums.BaseServiceExceptionEnum;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResult {

	private Integer code;
	
	private String message;
	

	private Object data;
	
	private HttpResult() {}

	public static HttpResult error(BaseServiceExceptionEnum exceptionEnum) {
		HttpResult result = new HttpResult();
		result.setCode(exceptionEnum.getCode());
		result.setMessage(exceptionEnum.getMessage());
		return result;
	}
	
	public static HttpResult error(Integer code, String message) {
		HttpResult result = new HttpResult();
		result.setCode(code);
		result.setMessage(message);
		return result;
	}
	
	public static HttpResult success() {
		HttpResult result = new HttpResult();
		result.setCode(BaseServiceExceptionEnum.REQUEST_SUCCESS.getCode());
		result.setMessage(BaseServiceExceptionEnum.REQUEST_SUCCESS.getMessage());
		return result;
	}
	
	public static HttpResult success(Object data) {
		HttpResult result = new HttpResult();
		result.setCode(BaseServiceExceptionEnum.REQUEST_SUCCESS.getCode());
		result.setMessage(BaseServiceExceptionEnum.REQUEST_SUCCESS.getMessage());
		result.setData(data);
		return result;
	}
	
	
	
}
