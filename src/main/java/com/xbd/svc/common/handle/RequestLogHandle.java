package com.xbd.svc.common.handle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.spring.web.json.Json;

@Slf4j
@Aspect
@Component
public class RequestLogHandle {
	
	private final String pointcut = "execution(public * com.xbd.svc.*.controller..*(..))";
	
	ThreadLocal<Long> startTime = new ThreadLocal<>();

	@Pointcut(pointcut)
	public void log() {
		
	}
	
	@Before("log()")
	public void doBefore(JoinPoint joinPoint) {
		
        if (log.isDebugEnabled()) {
        	startTime.set(System.currentTimeMillis());
        	printRequestInfo(joinPoint);
        }
	}
	
	@AfterReturning(returning = "ret", pointcut = "log()")
    public void doAfterReturning(Object ret) throws Throwable {
        if (log.isDebugEnabled()) {
        	//执行时间(秒)
        	//float requestTime = (float)(System.currentTimeMillis() - startTime.get()) / 1000;
        	long requestTime = (System.currentTimeMillis() - startTime.get());
        	if (requestTime > 1000) {
        		log.debug("请求消耗时间 : {} ms, 请求执行缓慢!", requestTime);
        	}
        	
            // 处理完请求，返回内容
        	if (ret != null) {
        		log.debug("请求返回结果 :\n {}", JSON.toJSONString(ret));
        	}
        }
    }

	
	private static String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
	
	/**
	 * 日志打印当前请求信息
	 * @param request
	 */
	private void printRequestInfo(JoinPoint joinPoint) {
		
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		//请求Url
		String requestURL = request.getRequestURL().toString();
        String url = request.getQueryString() == null ? requestURL + "" : (requestURL + "?" + request.getQueryString());
        //请求Ip
        String ip = getRemoteAddress(request);
        //请求方法类型
        String method = request.getMethod();
        //目标方法
        String javaMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
    	log.debug("当前请求信息============================================\n HttpUrl: {}\n javaMethod:{}\n HttpMethod: {}\n clientIp: {}\n", url, javaMethod, method, ip);
		
		Enumeration parameterNames = request.getParameterNames();
		StringBuilder paramsStr = new StringBuilder(); 
		while(parameterNames.hasMoreElements()) {
			String paramName = (String)parameterNames.nextElement();
			String[] parameterValues = request.getParameterValues(paramName);
			if (parameterValues.length == 1) {
				String paramValue = parameterValues[0];
				if (paramValue.length() != 0) {
					paramsStr.append(paramName)
							 .append("=")
							 .append(paramValue)
							 .append(",");
				}
			}
		}
		if (paramsStr.length() > 0) {
			log.debug("params: {}", paramsStr.substring(0, paramsStr.length() - 1));
		}
		
	}
}
