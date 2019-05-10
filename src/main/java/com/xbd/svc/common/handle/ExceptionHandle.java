package com.xbd.svc.common.handle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.xbd.svc.common.utils.IpAddrUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
	 * 用来处理方法里的单个/多个参数校验异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public HttpResult resolveConstraintViolationException(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
		if (!CollectionUtils.isEmpty(constraintViolations)) {
			StringBuilder msgBuilder = new StringBuilder();
			for (ConstraintViolation constraintViolation : constraintViolations) {
				log.info("请求参数错误: {}", constraintViolation.getMessage());
				return HttpResult.error(1111, "参数错误");
			}
		}
		return HttpResult.error(9999, "系统繁忙");
	}

	/**
	 * 用于处理@RequestBody实体中的参数校验异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public HttpResult resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
		if (!CollectionUtils.isEmpty(objectErrors)) {
			StringBuilder msgBuilder = new StringBuilder();
			for (ObjectError objectError : objectErrors) {
				//验证模式已设置为快速失败,所以拿到第一个错误就直接返回
				log.info("请求参数错误: {}", objectError.getDefaultMessage());
				return HttpResult.error(1111, "参数错误");
			}
		}
		return HttpResult.error(9999, "系统繁忙");
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
