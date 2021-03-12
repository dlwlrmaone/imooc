package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    public CorsConfig() {
    }

    @Bean
    public CorsFilter corsFilter(){

        //1.添加Cors配置信息
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedOrigin("http://localhost:8080");
        corsConfig.addAllowedOrigin("http://47.113.217.116:8080");
        corsConfig.addAllowedOrigin("http://47.113.217.116:8088");
        corsConfig.addAllowedOrigin("http://47.113.217.116");
        corsConfig.setAllowCredentials(true);//设置是否发送cookie信息
        //为url提供映射路径
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
        corsConfigSource.registerCorsConfiguration("/**",corsConfig);
        //返回重新定义好的url
        return new CorsFilter(corsConfigSource);
    }
}
