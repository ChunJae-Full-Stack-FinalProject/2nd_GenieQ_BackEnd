package com.cj.genieq.member.service;

import com.cj.genieq.member.dto.request.SignUpRequestDto;

public interface AuthService {
    boolean checkEmailDuplicate(String email); // 중복 이메일 검사
    void signUp(SignUpRequestDto signUpRequestDto); // 회원가입
}
