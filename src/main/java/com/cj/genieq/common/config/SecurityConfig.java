package com.cj.genieq.common.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/configuration/**").permitAll() // Swagger 허용
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll() // API 요청에 대한 접근 허용
                        .requestMatchers("/api/auth/**").permitAll() // 회원 인증
                        .requestMatchers("/api/info/**").permitAll() // 회원 정보
                        .requestMatchers("/tick/**").permitAll() // 이용권
                        .requestMatchers("/paym/**").permitAll() // 결제
                        .requestMatchers("/subj/**").permitAll() // 지문주제
                        .requestMatchers("/pass/**").permitAll() // 지문
                        .requestMatchers("/favo/**").permitAll() // 즐겨찾기
                        .requestMatchers("/form/**").permitAll() // 문항형식
                        .requestMatchers("/ques/**").permitAll() // 문항
                        .requestMatchers("/noti/**").permitAll() // 공지
                        .requestMatchers("/api/usag/**").permitAll() // 이용내역
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
//                .logout(logout -> logout
//                        .logoutUrl("/api/auth/select/logout") // 로그아웃 URL 지정
//                        .invalidateHttpSession(true) // 세션 무효화
//                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
//                        .logoutSuccessHandler((request, response, authentication) -> {
//                            response.setCharacterEncoding("UTF-8");
//                            response.setStatus(HttpServletResponse.SC_OK);
//                            response.getWriter().write("로그아웃 성공 및 쿠키 삭제 완료");
//                        })
//                )
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // 자격 증명 허용 (세션, 쿠키 등)
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 메서드 설정
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
