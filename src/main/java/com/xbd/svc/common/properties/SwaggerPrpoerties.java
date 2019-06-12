package com.xbd.svc.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger可配置属性
 */
@ConfigurationProperties(prefix = SwaggerPrpoerties.XBD_SVC_REQUEST_SWAGGER_PREFIX)
@Data
public class SwaggerPrpoerties {

    public static final String XBD_SVC_REQUEST_SWAGGER_PREFIX = "xbd.svc.swagger";

    public static final String TITLE_SUFFIX = " RESTful APIs";

    /**
     * 是否开启Swagger,默认不开启
     */
    private boolean enable;

    /**
     * api文档的路径,默认设置为 /v2/api-docs
     */
    private String location = "/v2/api-docs";

    /**
     * 文档标题, 如果yml/properties配置文件配置了spring.applicationName,
     *   默认会使用配置文件中[spring.applicationName] + RESTful APIs
     */
    private String title = TITLE_SUFFIX;

    /**
     * 文档描述
     */
    private String description = "apiDocument";

    /**
     * 接口文档版本,默认设置1.0
     */
    private String apiVersion = "1.0";

    /**
     * Swagger框架版本,默认设置为2
     */
    private String swaggerVersion = "2.0";

    /**
     * 联系人属性
     */
    private ContactProperties contact = new ContactProperties();


    /**
     * 联系人信息
     */
    @Data
    public static class ContactProperties {
        public static final String CONTACT_UNKNOWN_NAME = "UnSetContactName";
        public static final String CONTACT_UNKNOWN_EMAIL = "UnSetContactEmail";
        public static final String CONTACT_UNKNOWN_URL = "localhost";

        /**
         * Swagger-ui 页面上显示的联系人姓名
         */
        private String name = CONTACT_UNKNOWN_NAME;

        /**
         * Swagger-ui 页面上显示的联系人邮箱
         */
        private String email = CONTACT_UNKNOWN_EMAIL;

        /**
         * Swagger-ui 页面上显示的  "See more at" {url}
         * url
         */
        private String url = CONTACT_UNKNOWN_URL;

    }

}
