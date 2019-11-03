package com.xbd.svc.common.handle;

import com.xbd.svc.common.enums.BaseServiceExceptionEnum;
import com.xbd.svc.common.exception.BaseServiceException;
import com.xbd.svc.common.exception.RemoteServiceException;
import com.xbd.svc.common.exception.RemoteServiceUnknownException;
import com.xbd.svc.common.model.HttpResult;
import com.xbd.svc.common.utils.IpAddrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

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
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public HttpResult exceptionHandle(HttpServletRequest request, Exception e) {
		//系统中未知异常
		log.error("【系统异常出错】============================================================");
		//打印当前请求信息
		printErrorRequestInfo(request);
		e.printStackTrace();
		return HttpResult.error(BaseServiceExceptionEnum.SYS_ERROR);
	}

	
	/**
	 * 处理业务自定义异常
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BaseServiceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public HttpResult exceptionHandle(HttpServletRequest request, BaseServiceException e) {
		log.error("【系统异常出错】============================================================");
		//打印当前请求信息
		printErrorRequestInfo(request);
		e.printStackTrace();
		return HttpResult.error(e.getCode(), e.getMessage());
	}

	/**
	 * 内部微服务抛出的已知异常
	 * feign errorDecode 处理503请求为内部服务异常,以此在服务中传递异常
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(RemoteServiceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public HttpResult exceptionHandle(HttpServletRequest request, RemoteServiceException e) {
		log.error("【系统异常出错】============================================================");
		//打印当前请求信息
		printErrorRequestInfo(request);
		e.printStackTrace();
		return HttpResult.error(e.getCode(), e.getMessage());
	}

	/**
	 * 内部微服务抛出的未知异常
	 * feign执行成功状态码为200，但是内部逻辑出错，抛出未知的错误，被统一异常处理的exceptionHandle转换为错误结果
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(RemoteServiceUnknownException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public HttpResult exceptionHandle(HttpServletRequest request, RemoteServiceUnknownException e) {
		log.error("【系统异常出错】============================================================");
		//打印当前请求信息
		printErrorRequestInfo(request);
		//对内只打印错误信息
		e.printStackTrace();
		//对外转换为统一异常
		return HttpResult.error(BaseServiceExceptionEnum.REMOTE_SERVICE_EXCEPTION);
	}

	/**
	 * 用来处理方法里的单个/多个参数校验异常
	 * 返回状态码为400是为了使用springcloud时 不触发hystrix 的熔断机制，客户端请求错误并不是服务本身错误
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public HttpResult resolveConstraintViolationException(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
		if (!CollectionUtils.isEmpty(constraintViolations)) {
			for (ConstraintViolation constraintViolation : constraintViolations) {
				log.info("请求参数错误, {} : {}", constraintViolation.getPropertyPath(), constraintViolation.getMessage());
			}
		}
		return HttpResult.error(BaseServiceExceptionEnum.PARAM_ERROR);
	}

	/**
	 * 用于处理@RequestBody实体中的参数校验异常
	 * 返回状态码为400是为了使用springcloud时 不触发hystrix 的熔断机制，客户端请求错误并不是服务本身错误
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public HttpResult resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		if (!CollectionUtils.isEmpty(fieldErrors)) {
			for (FieldError fieldError : fieldErrors) {
				//验证模式已设置为快速失败,所以拿到第一个错误就直接返回
				log.info("请求参数错误, {} : {}", fieldError.getField(), fieldError.getDefaultMessage());
			}
		}
		return HttpResult.error(BaseServiceExceptionEnum.PARAM_ERROR);
	}

	/**
	 * 用于处理Get请求,实体中的参数校验异常
	 * 返回状态码为400是为了使用springcloud时 不触发hystrix 的熔断机制，客户端请求错误并不是服务本身错误
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public HttpResult validExceptionHandler(BindException e) {
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		for (FieldError fieldError:fieldErrors){
			log.info("请求参数错误, {} : {}", fieldError.getField(), fieldError.getDefaultMessage());
		}
		return HttpResult.error(BaseServiceExceptionEnum.PARAM_ERROR);
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
