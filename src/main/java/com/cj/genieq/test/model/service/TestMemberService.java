package com.cj.genieq.test.model.service;

import com.cj.genieq.test.model.dto.TestMember;

public interface TestMemberService {
    TestMember saveMember(TestMember member);
    TestMember findByMemberCode(Long code);
}
