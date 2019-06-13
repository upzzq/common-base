package com.xbd.svc.common.feign.support;

import com.alibaba.fastjson.JSONObject;
import com.xbd.svc.common.exception.RemoteServiceException;
import com.xbd.svc.common.model.HttpResult;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;

import java.io.IOException;
import java.lang.reflect.Type;

@Slf4j
public class FeignResponseEntityDecoder extends ResponseEntityDecoder {

    public FeignResponseEntityDecoder(Decoder decoder) {
        super(decoder);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        String feignResponseJson = response.body().toString();
        String feignRequestUrl = response.request().url();
        checkIsExceptionResponse(feignResponseJson, feignRequestUrl);
        return super.decode(response, type);
    }

    private void checkIsExceptionResponse(String feignResponseJson, String feignRequestUrl) {
        HttpResult feignHttpResult = null;
        try {
            //如果类型转成功,说明该返回结果是内部服务的未知错误并且response的状态为200, 并转换为httpResult
            feignHttpResult = JSONObject.parseObject(feignResponseJson, HttpResult.class);
        } catch (Exception e) {
            //转换错误说明接口正确返回,不做任何处理
            return;
        }

        if (feignHttpResult != null) {
            log.error("远程服务处理失败,服务内部未捕获的异常,请求url:\r\n{}\r\n{}", feignRequestUrl, feignResponseJson);
            throw new RemoteServiceException(feignHttpResult.getCode(), feignHttpResult.getMessage());
        }
    }
}
