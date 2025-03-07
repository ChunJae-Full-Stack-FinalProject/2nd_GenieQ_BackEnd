package com.cj.genieq.test.service;

import com.cj.genieq.test.dto.TestMember;

public interface TestMemberService {
    TestMember saveMember(TestMember member);
    TestMember findByMemberCode(Long code);
}
