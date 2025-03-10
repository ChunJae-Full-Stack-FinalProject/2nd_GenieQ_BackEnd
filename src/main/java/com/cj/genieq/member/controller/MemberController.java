package com.cj.genieq.member.controller;

import com.cj.genieq.member.dto.request.LoingReuestDto;
import com.cj.genieq.member.dto.request.SignUpRequestDto;
import com.cj.genieq.member.dto.response.LoginMemberResponseDto;
import com.cj.genieq.member.dto.response.MemberInfoResponseDto;
import com.cj.genieq.member.service.AuthService;
import com.cj.genieq.member.service.InfoService;
import com.cj.genieq.test.service.TestMemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> login(@RequestBody LoingReuestDto loinReuestDto, HttpSession session){
        authService.login(loinReuestDto.getMemEmail(), loinReuestDto.getMemPassword(), session);
        return ResponseEntity.ok().body("로그인 성공");
    }

    // Info Controller
    
    // 회원 정보 전체 조회
    @GetMapping("/info/select/entire")
    public ResponseEntity<?> selectEntire(HttpSession session){
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        System.out.println(loginMember);

        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        } else {
            MemberInfoResponseDto memberInfo = infoService.getMemberInfo(loginMember.getMemberCode());

            return ResponseEntity.ok().body(memberInfo);
        }
    }
}
