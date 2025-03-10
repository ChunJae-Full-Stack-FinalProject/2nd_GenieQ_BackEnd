package com.cj.genieq.member.service;

import com.cj.genieq.member.dto.response.MemberInfoResponseDto;
import com.cj.genieq.member.entity.MemberEntity;
import com.cj.genieq.member.repository.MemberRepository;
import com.cj.genieq.usage.repository.UsageRepository;
import jakarta.persistence.EntityNotFoundException;
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
}
