package com.xbd.svc.common.exception;

import lombok.Getter;

@Getter
public class BaseServiceException extends RuntimeException {

	private Integer code;
	
	public BaseServiceException(Integer code, String message) {
		super(message);
		this.code = code;
	}

}
