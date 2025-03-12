package com.cj.genieq.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // REST API 백엔드 구현을 위한 중요한 설정 파일
    // CORS 설정, 컨트롤러 요청 매핑 및 변환 설정, 인터셉터 등록

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins( "http://localhost:5173")// Vue 프로젝트의 개발 서버 주소
                .allowedMethods("GET", "POST", "PUT", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
