package com.imooc.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    //实现静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**")
                //映射swagger2
                .addResourceLocations("classpath:/META-INF/resources/")
                //映射本地静态资源(dev)
                .addResourceLocations("file:/Users/dlwlrmaone/Downloads/foodie_docs/");

    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){

        return builder.build();
    }
}
