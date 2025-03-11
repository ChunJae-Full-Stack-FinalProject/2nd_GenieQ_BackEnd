package com.cj.genieq.member.controller;

import com.cj.genieq.member.dto.request.*;
import com.cj.genieq.member.dto.response.LoginMemberResponseDto;
import com.cj.genieq.member.dto.response.MemberInfoResponseDto;
import com.cj.genieq.member.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.cj.genieq.member.service.InfoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController //컨트롤러에서 반환된 값이 JSON 형태로 응답됨
@RequestMapping("/api")
@RequiredArgsConstructor //자동 생성자 주입

public class MemberController {

    private final AuthService authService;
    private final InfoService infoService;

    // Auth Controller

    // 회원가입 API
    @PostMapping("/auth/insert/signup")
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

    @PostMapping("/auth/select/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loinRequestDto, HttpSession session){
        LoginMemberResponseDto loginuser = authService.login(loinRequestDto.getMemEmail(), loinRequestDto.getMemPassword(), session);
        return ResponseEntity.ok().body(loginuser);
    }

    @PutMapping("/auth/remove/withdrawal")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequestDto withdrawRequestDto, HttpSession session){
        authService.withdraw(withdrawRequestDto.getMemEmail(), session);
        return ResponseEntity.ok("탈퇴완료");
    }

    @PostMapping("/auth/select/logout")
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

    // Info Controller

    // 회원 정보 전체 조회
    @GetMapping("/info/select/entire")
    public ResponseEntity<?> selectEntire(HttpSession session){
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        } else {
            MemberInfoResponseDto memberInfo = infoService.getMemberInfo(loginMember.getMemberCode());

            return ResponseEntity.ok().body(memberInfo);
        }
    }

    // 회원의 잔여 이용권 조회
    @GetMapping("/info/select/ticket")
    public ResponseEntity<?> selectTicket(HttpSession session){
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        int balance = infoService.getUsageBalance(loginMember.getMemberCode());
        return ResponseEntity.ok(balance);
    }

    @PatchMapping("/info/update/name")
    public ResponseEntity<String> updateName(@RequestBody UpdateNameRequestDto updateNameRequestDto, HttpSession session){
        infoService.updateName(updateNameRequestDto.getMemName(), session);
        return ResponseEntity.ok("이름 수정 완료");
    }

    @PatchMapping("/info/update/type")
    public ResponseEntity<String> updateType(@RequestBody UpdateTypeRequestDto updateTypeRequestDto, HttpSession session){
        infoService.updateType(updateTypeRequestDto.getMemType(), session);
        return ResponseEntity.ok("소속 수정 완료");
    }

    @PatchMapping("/info/update/password")
    public ResponseEntity<String> updatePassword(
            @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto,
            HttpSession session
            ){
        infoService.updatePassword(
                updatePasswordRequestDto.getCurrentPassword(),
                updatePasswordRequestDto.getNewPassword(),
                updatePasswordRequestDto.getConfirmPassword(),
                session
        );
        return ResponseEntity.ok("비밀번호 수정 완료");
    }
}
