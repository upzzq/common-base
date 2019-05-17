package com.xbd.svc.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class Swagger2Config {

	@Value("${spring.application.name}")
    public String appName;
	
	 @Bean
    public Docket createRestApi() {
       /* ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").defaultValue("Bearer ").description("jwt令牌").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
        pars.add(tokenPar.build());
        return (new Docket(DocumentationType.SWAGGER_2)).apiInfo(this.apiInfo()).select().paths(PathSelectors.any()).build().globalOperationParameters(pars);*/
       
        return (new Docket(DocumentationType.SWAGGER_2))
        		.apiInfo(this.apiInfo())
        		.select()
        		.paths(PathSelectors.any())
        		.build();
    }

    private ApiInfo apiInfo() {
        //return (new ApiInfoBuilder()).title("[" + this.appName + "] RESTful APIs").description("开发测试接口文档").termsOfServiceUrl("http://uinpay.com/").version("1.0").build();
    	return new ApiInfoBuilder()
    			//页面标题
    			.title("[" + this.appName + "] RESTful APIs")
    			//创建人
    			.contact(new Contact("hkzzq", "www.baidu.com", "hk123@gmail.com"))
    			//版本号
    			.version("1.0")
    			//描述
    			.description("接口文档")
    			.build();
    }
}
