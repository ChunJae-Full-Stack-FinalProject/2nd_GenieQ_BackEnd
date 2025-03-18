package com.cj.genieq.common.session;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class SessionActivityFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        // 세션이 존재하고, 세션 갱신 요청이 아닐 경우에만 세션 활동 기록
        if (session != null && !"/api/session/keep-alive".equals(httpRequest.getRequestURI())) {
            // 마지막 활동 시간 기록
            session.setAttribute("lastActivity", System.currentTimeMillis());
            System.out.println("세션 활동 감지: "+ session.getId());
            log.info("세션 활동 감지: {}", session.getId());
        }

        chain.doFilter(request, response);
    }
}