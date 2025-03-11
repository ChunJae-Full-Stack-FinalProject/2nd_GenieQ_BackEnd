package com.cj.genieq.payment.controller;

import com.cj.genieq.member.dto.response.LoginMemberResponseDto;
import com.cj.genieq.payment.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paym")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    // 결제 내역 추가
    @GetMapping("/insert/each")
    public ResponseEntity<?> insertEach(HttpSession session, @RequestParam(required = false) Long ticCode) {
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        paymentService.insertPayment(loginMember.getMemberCode(), ticCode);

        return ResponseEntity.ok().body("결제 성공");
    }
}
