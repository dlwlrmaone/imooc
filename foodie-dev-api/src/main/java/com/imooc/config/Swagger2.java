package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

    //前端API页面地址：http://localhost:8088/swagger-ui.html   原路径
    //换肤API页面地址：http://localhost:8088/doc.html  换肤后路径

    //进行Swagger2核心配置 docket
    @Bean
    public Docket createRestApi(){

        //指定API类型为Swagger2
        Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(createApiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.imooc.controller")).paths(PathSelectors.any()).build();
        return docket;

    }

    private ApiInfo createApiInfo(){

        Contact dlwlrmaone = new Contact("dlwlrmaone", "https://www.imoocfoodie.com", "spaceloviu@163.com");
        ApiInfo apiInfo = new ApiInfoBuilder().title("天天吃货电商平台API").contact(dlwlrmaone)
                .description("项目经理：张宇，后端开发：张宇，前端开发：赵敏，UI设计：李宁，产品经理：张宇，dev测试：张宇，uat测试：李宁，运维经理：张宇，编写时间：2021.01.05 22:00")
                .version("1.0.1").termsOfServiceUrl("https://www.imoocfoodie.com")
                .license("中华人民共和国网络监管部特批").licenseUrl("https://www.baidu.com").build();
        return apiInfo;
    }
}
