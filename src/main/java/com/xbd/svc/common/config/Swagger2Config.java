package com.xbd.svc.common.config;

import com.xbd.svc.common.properties.SwaggerPrpoerties;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@Slf4j
@ConditionalOnClass(Swagger.class)
public class Swagger2Config {

    @Value("${spring.application.name}")
    public String applicationName;

    @Autowired
    private SwaggerPrpoerties swaggerPrpoerties;

    @Bean
    @ConditionalOnMissingBean(name = "createRestApi")
    public Docket createRestApi() {
       /* ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").defaultValue("Bearer ").description("jwt令牌").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
        pars.add(tokenPar.build());
        return (new Docket(DocumentationType.SWAGGER_2)).apiInfo(this.apiInfo()).select().paths(PathSelectors.any()).build().globalOperationParameters(pars);*/
        boolean isEnable = swaggerPrpoerties.isEnable();
        if (log.isDebugEnabled()) {
            log.debug("Swagger2 已配置生效");
        }
        return (new Docket(DocumentationType.SWAGGER_2))
                .enable(isEnable)
                .apiInfo(this.apiInfo())
                .select()
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        //return (new ApiInfoBuilder()).title("[" + this.appName + "] RESTful APIs").description("开发测试接口文档").termsOfServiceUrl("http://uinpay.com/").version("1.0").build();
        SwaggerPrpoerties.ContactProperties contact = swaggerPrpoerties.getContact();

        String appName = applicationName;

        String title = swaggerPrpoerties.getTitle();
        String contactName = contact.getName();
        String contactUrl = contact.getUrl();
        String contactEmail = contact.getEmail();
        String apiVersion = swaggerPrpoerties.getApiVersion();
        String description = swaggerPrpoerties.getDescription();

        if (StringUtils.isNotBlank(appName)) {
            // 如果 yml/properties 文件中设置了spring.application.name,就使用此属性当做title前缀
            title = "[" + appName + "]" + title;
        }

        return new ApiInfoBuilder()
                // 页面标题
                .title(title)
                // 创建人
                .contact(new Contact(contactName, contactUrl, contactEmail))
                // 版本号
                .version(apiVersion)
                // 描述
                .description(description)
                .build();
    }


}
