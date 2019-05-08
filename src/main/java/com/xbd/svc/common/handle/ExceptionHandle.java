package com.xbd.svc.common.handle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.xbd.svc.common.utils.IpAddrUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.xbd.svc.common.enums.ResultEnum;
import com.xbd.svc.common.exception.BaseServiceException;
import com.xbd.svc.common.model.HttpResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionHandle {

	/**
	 * 处理系统未知异常
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public HttpResult exceptionHandle(HttpServletRequest request, Exception e) {
		//系统中未知异常
		log.error("【系统异常出错】============================================================");
		//打印当前请求信息
		printErrorRequestInfo(request);
		e.printStackTrace();
		return HttpResult.error(ResultEnum.SYS_ERROR);
	}

	
	/**
	 * 处理业务自定义异常
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BaseServiceException.class)
	public HttpResult exceptionHandle(HttpServletRequest request, BaseServiceException e) {
		log.error("【系统异常出错】============================================================");
		//打印当前请求信息
		printErrorRequestInfo(request);
		e.printStackTrace();
		return HttpResult.error(e.getCode(), e.getMessage());
	}
	

	
	/**
	 * 日志打印当前请求信息
	 * @param request
	 */
	private void printErrorRequestInfo(HttpServletRequest request) {
		
		//当前时间
		LocalDateTime nowLocalDateTime = LocalDateTime.now();
		DateTimeFormatter stf = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时:mm分:ss秒");
		String dateStr = stf.format(nowLocalDateTime);
		//ip地址
		String ip = IpAddrUtil.getRemoteAddress(request);
		//请求Url
		String requestURL = request.getRequestURL().toString();
        String url = request.getQueryString() == null ? requestURL + "" : (requestURL + "?" + request.getQueryString());
		log.error("异常时间: {}\n 请求ip: {}\n 请求url : {}\n", dateStr, ip, url);
		
		Enumeration parameterNames = request.getParameterNames();
		while(parameterNames.hasMoreElements()) {
			String paramName = (String)parameterNames.nextElement();
			String[] parameterValues = request.getParameterValues(paramName);
			if (parameterValues.length == 1) {
				String paramValue = parameterValues[0];
				if (paramValue.length() != 0) {
					log.error("params：" + paramName + "=" + paramValue + "\n");
				}
			}
		}
	}

}
