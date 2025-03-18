package com.cj.genieq.common.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/session")
public class SessionController {

    @GetMapping("/keep-alive")
    public ResponseEntity<String> keepSessionAlive(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            // 세션이 존재하면 마지막 활동 시간 업데이트
            session.setAttribute("lastActivity", System.currentTimeMillis());
            System.out.println("세션 갱신: " + session.getId());
            log.info("세션 갱신: {}", session.getId());
            return ResponseEntity.ok("Session refreshed");
        }

        return ResponseEntity.ok("No active session");
    }
}
