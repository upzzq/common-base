package com.xbd.svc.common.exception;

import com.xbd.svc.common.enums.BaseServiceExceptionEnum;

/**
 * 内部微服务捕获的异常
 */
public class RemoteServiceException extends BaseServiceException {

    public RemoteServiceException(Integer code, String message) {
        super(code, message);
    }

    public RemoteServiceException(BaseServiceExceptionEnum exceptionEnum) {
        super(exceptionEnum.getCode(), exceptionEnum.getMessage());
    }
}
