package com.xbd.svc.common.exception;

import com.xbd.svc.common.enums.BaseServiceExceptionEnum;

/**
 * 内部微服务未捕获的异常
 */
public class RemoteServiceUnknownException extends RemoteServiceException {

    public RemoteServiceUnknownException(Integer code, String message) {
        super(code, message);
    }

    public RemoteServiceUnknownException(BaseServiceExceptionEnum exceptionEnum) {
        super(exceptionEnum.getCode(), exceptionEnum.getMessage());
    }
}
