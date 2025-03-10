package com.cj.genieq.member.service;

import com.cj.genieq.member.dto.request.UpdateNameRequestDto;
import com.cj.genieq.member.dto.response.LoginMemberResponseDto;
import com.cj.genieq.member.dto.response.MemberInfoResponseDto;
import com.cj.genieq.member.entity.MemberEntity;
import com.cj.genieq.member.repository.MemberRepository;
import com.cj.genieq.usage.repository.UsageRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {
    private final MemberRepository memberRepository;
    private final UsageRepository usageRepository;

    @Override
    public MemberInfoResponseDto getMemberInfo(Long memCode) {
        MemberEntity member = memberRepository.findByMemCode(memCode)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보가 존재하지 않습니다."));

        MemberInfoResponseDto memberInfo = MemberInfoResponseDto.builder()
                .memCode(member.getMemCode())
                .name(member.getMemName())
                .email(member.getMemEmail())
                .gender(member.getMemGender())
                .memType(member.getMemType())
                .build();

        return memberInfo;
    }

    @Override
    public int getUsageBalance(Long memCode) {
        int usageBalance = usageRepository.findBalanceByMemberCode(memCode)
                .stream()
                .findFirst()
                .orElse(0);

        return usageBalance;
    }

    @Override
    @Transactional
    public void updateName(String memName, HttpSession session) {
        // 세션에서 DTO로 가져오기
        LoginMemberResponseDto loginUser = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        if (loginUser == null) {
            throw new IllegalArgumentException("세션이 만료되었거나 로그인되지 않았습니다.");
        }


        // 이메일 가져오기
        String memEmail = loginUser.getEmail();

        //이메일로 사용자 조회
        MemberEntity member = memberRepository.findByMemEmail(memEmail)
                .orElseThrow(()-> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        //이름 수정 후 저장
        member.setMemName(memName);
        memberRepository.save(member);
    }
}
