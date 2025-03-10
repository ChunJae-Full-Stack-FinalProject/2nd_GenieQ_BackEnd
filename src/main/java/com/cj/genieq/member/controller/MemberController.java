package com.cj.genieq.member.controller;

import com.cj.genieq.member.dto.request.LoingReuestDto;
import com.cj.genieq.member.dto.request.SignUpRequestDto;
import com.cj.genieq.member.dto.request.WithdrawRequestDto;
import com.cj.genieq.member.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController //컨트롤러에서 반환된 값이 JSON 형태로 응답됨
@RequestMapping("/auth")
@RequiredArgsConstructor //자동 생성자 주입

public class MemberController {

    private final AuthService authService;

    // 회원가입 API
    @PostMapping("/insert/signup")
    // ResponseEntity<?>를 사용하면 HTTP 상태 코드(200, 400, 401, 404, 500 등)와 함께 응답 데이터를 클라이언트에 명확하게 전달
    // @RequestBody는 클라이언트에서 받은 JSON 값을 객체로 매핑
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        // 이메일 중복 체크
        if (authService.checkEmailDuplicate(signUpRequestDto.getMemEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
        }

        // 회원가입 처리
        authService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");


    }

    @PostMapping("/select/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loinReuestDto, HttpSession session){
        authService.login(loinReuestDto.getMemEmail(), loinReuestDto.getMemPassword(), session);
        return ResponseEntity.ok().body("로그인 성공");
    }

    @PutMapping("/remove/withdrawal")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequestDto withdrawRequestDto, HttpSession session){
        authService.withdraw(withdrawRequestDto.getMemEmail(), session);
        return ResponseEntity.ok("탈퇴완료");
    }

    @PostMapping("/select/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        SecurityContextHolder.clearContext(); // Security 컨텍스트 삭제

        // JSESSIONID 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().body("로그아웃 성공");
    }

}
