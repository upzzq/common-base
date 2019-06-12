package com.xbd.svc.common.exception;

import com.xbd.svc.common.enums.BaseServiceExceptionEnum;
import lombok.Data;
import lombok.Getter;

@Data
public class BaseServiceException extends RuntimeException {

	private Integer code;

	private String message;

	public BaseServiceException(Integer code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public BaseServiceException(BaseServiceExceptionEnum exceptionEnum) {
		super(exceptionEnum.getMessage());
		this.code = exceptionEnum.getCode();
		this.message = exceptionEnum.getMessage();
	}
}
