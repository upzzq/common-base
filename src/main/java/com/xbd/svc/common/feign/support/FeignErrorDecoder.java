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
import org.apache.commons.lang.StringUtils;
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
            log.error("远程服务处理失败,请求url:\r\n{}\r\n{}" , errorRequestUrl , errorContent);
            if (StringUtils.isNotBlank(errorContent)) {
                if (response.status() != HttpStatus.OK.value()) {

                    JSONObject errorMsg = JSON.parseObject(errorContent);
                    int code = errorMsg.getInteger("code");
                    String message = errorMsg.getString("message");

                    //基础服务抛出的请求异常不触发 hystrix 的熔断机制
                    if (400 <= response.status() && response.status() < 500) {
                        return new HystrixBadRequestException("errorDecode: request error", new RemoteServiceException(BaseServiceExceptionEnum.REMOTE_SERVICE_ERROR));
                    }

                    //status 503 是统一异常处理抛出的微服务调用异常(异常传递)
                    if (response.status() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
                        return new RemoteServiceException(code, message);
                    }
                }
            }
            return new BaseServiceException(BaseServiceExceptionEnum.SYS_ERROR);
        } catch (IOException e) {
            log.error("errorDecode httpStatus 503 response 解析错误");
            e.printStackTrace();
            return new BaseServiceException(BaseServiceExceptionEnum.SYS_ERROR);
        }
    }
}
