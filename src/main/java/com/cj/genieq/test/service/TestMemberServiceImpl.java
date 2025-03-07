package com.cj.genieq.test.service;

import com.cj.genieq.test.dto.TestMember;
import com.cj.genieq.test.entity.TestMemberEntity;
import com.cj.genieq.test.repository.TestMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestMemberServiceImpl implements TestMemberService{
    private final TestMemberRepository repository;

    @Override
    public TestMember saveMember(TestMember member) {
        TestMemberEntity entity = TestMemberEntity.fromTestMember(member);
        TestMember memberDTO = repository.save(entity).toTestMember();
        return memberDTO;
    }

    @Override
    public TestMember findByMemberCode(Long code) {
        return repository.findByMemberCode(code)
                .map(TestMemberEntity::toTestMember)  // 엔티티를 DTO로 변환
                .orElse(null);  // 결과가 없으면 null 반환
    }
}
