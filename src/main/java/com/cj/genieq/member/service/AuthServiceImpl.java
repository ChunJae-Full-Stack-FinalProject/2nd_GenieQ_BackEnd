package com.cj.genieq.member.service;

import com.cj.genieq.member.dto.request.SignUpRequestDto;
import com.cj.genieq.member.entity.MemberEntity;
import com.cj.genieq.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 이메일 중복 검사 서비스
    // 이메일 하나만을 전달받아 중복 체크를 수행
    @Override
    public boolean checkEmailDuplicate(String email) {
        // 1이면 중복, 0이면 사용 가능
        return memberRepository.existsByMemEmail(email)==1;
    }

    //  회원가입 처리
    @Override
    // signUp(SignUpRequestDto signUpRequestDto)는 회원가입에 필요한 데이터를 매개변수로 받음
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (checkEmailDuplicate(signUpRequestDto.getMemEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getMemPassword());

        // 엔티티에 저장할 값 세팅
        MemberEntity member = MemberEntity.builder()
                .memName(signUpRequestDto.getMemName())
                .memEmail(signUpRequestDto.getMemEmail())
                .memPassword(encodedPassword)
                .memGender(signUpRequestDto.getMemGender())
                .memType(signUpRequestDto.getMemType())
                .build();

        // db에 저장 처리
        memberRepository.save(member);
    }
}
