package com.xbd.svc.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xbd.svc.common.enums.ResultEnum;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResult {

	private Integer code;
	
	private String message;
	

	private Object data;
	
	private HttpResult() {}

	public static HttpResult error(ResultEnum resultEnum) {
		HttpResult result = new HttpResult();
		result.setCode(resultEnum.getCode());
		result.setMessage(resultEnum.getMessage());
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
		result.setCode(ResultEnum.REQUEST_SUCCESS.getCode());
		result.setMessage(ResultEnum.REQUEST_SUCCESS.getMessage());
		return result;
	}
	
	public static HttpResult success(Object data) {
		HttpResult result = new HttpResult();
		result.setCode(ResultEnum.REQUEST_SUCCESS.getCode());
		result.setMessage(ResultEnum.REQUEST_SUCCESS.getMessage());
		result.setData(data);
		return result;
	}
	
	
	
}
