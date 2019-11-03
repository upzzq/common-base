package com.xbd.svc.common.feign.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.xbd.svc.common.enums.BaseServiceExceptionEnum;
import com.xbd.svc.common.exception.BaseServiceException;
import com.xbd.svc.common.exception.RemoteServiceException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * feign调用错误时的处理,主要是异常传递
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            //解析接口返回结果
            String errorRequestUrl = response.request().url();
            String errorContent = Util.toString(response.body().asReader());
            log.error("远程服务处理失败,\r\nmethodKey: {}\r\n响应结果: {}\r\n请求url: {}" , methodKey, errorContent, errorRequestUrl);
            if (StringUtils.isNotBlank(errorContent)) {
                // 基础服务返回的 httpstatus 为 400 - 499 不触发 hystrix 的熔断机制
                if (HttpStatus.BAD_REQUEST.value() >= response.status() && response.status() < HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                    return new HystrixBadRequestException("errorDecode: request error", new RemoteServiceException(BaseServiceExceptionEnum.REMOTE_SERVICE_ERROR));
                }

                JSONObject errorMsg = JSON.parseObject(errorContent);
                Integer code = errorMsg.getInteger("code");
                String message = errorMsg.getString("message");

                if (code == null || message == null) {
                    // 未知错误信息
                    return new BaseServiceException(BaseServiceExceptionEnum.SYS_ERROR);
                }
                return new RemoteServiceException(code, message);
            } else {
                // 无错误信息
                return new BaseServiceException(BaseServiceExceptionEnum.SYS_ERROR);
            }

        } catch (IOException e) {
            log.error("errorDecode httpStatus response 解析错误", e);
            return new BaseServiceException(BaseServiceExceptionEnum.SYS_ERROR);
        }
    }
}
