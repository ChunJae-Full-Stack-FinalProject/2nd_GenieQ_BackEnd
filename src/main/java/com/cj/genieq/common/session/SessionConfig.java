package com.cj.genieq.common.session;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class SessionConfig {

    @Bean
    @Description("HttpSessionListener 등록")
    public ServletListenerRegistrationBean<SessionListener> sessionListenerRegistration() {
        ServletListenerRegistrationBean<SessionListener> registration = new ServletListenerRegistrationBean<>();
        registration.setListener(new SessionListener());
        return registration;
    }
}
