package com.xbd.svc.common.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 配置 hibernate的校验模式
 * 1、普通模式（默认是这个模式）
 * 普通模式(会校验完所有的属性，然后返回所有的验证失败信息)
 *
 * 2、快速失败返回模式
 * 快速失败返回模式(只要有一个验证失败，则返回)
 *
 * failFast：true 快速失败返回模式 false 普通模式
 */
@Configuration
public class ValidatorConfig {

    /**
     * 设置为快速失败
     * @return
     */
    @Bean
    public Validator validator(){

        //方式一
       /* ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .failFast( true )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();*/

       //方式二
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                //.failFast(true)
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();

        return validatorFactory.getValidator();
    }

    /**
     * 用于验证方法里的单个/多个参数(不加此配置无法对@RequestParam注解起作用)
     * 1.配置后需要在Controller类上加注解@Validated
     * 2.在方法的参数增加相对应的规则校验参数
     * @return
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        //默认是普通模式，会返回所有的验证不通过信息集合
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        //设置validator模式为快速失败返回,对MethodValidationPostProcessor 进行设置Validator（因为此时不是用的Validator进行验证，Validator的配置不起作用）
        methodValidationPostProcessor.setValidator(validator());
        return methodValidationPostProcessor;
    }
}
