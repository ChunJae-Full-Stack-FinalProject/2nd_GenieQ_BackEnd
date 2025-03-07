package com.cj.genieq.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 기능 비활성화 (REST API에서는 일반적으로 비활성화)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 요청에 대한 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/test/**").permitAll() // API 요청에 대한 접근 허용
                        .requestMatchers("/auth/**").permitAll() // 회원 인증
                        .requestMatchers("/info/**").permitAll() // 회원 정보
                        .requestMatchers("/tick/**").permitAll() // 이용권
                        .requestMatchers("/paym/**").permitAll() // 결제
                        .requestMatchers("/subj/**").permitAll() // 지문주제
                        .requestMatchers("/pass/**").permitAll() // 지문
                        .requestMatchers("/favo/**").permitAll() // 즐겨찾기
                        .requestMatchers("/form/**").permitAll() // 문항형식
                        .requestMatchers("/ques/**").permitAll() // 문항
                        .requestMatchers("/noti/**").permitAll() // 공지
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .build();

    }
}
