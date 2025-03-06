package com.cj.genieq.test.controller;

import com.cj.genieq.test.model.dto.TestMember;
import com.cj.genieq.test.model.service.TestMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/test/members")
@RequiredArgsConstructor
public class TestMemberController {

    private final TestMemberService service;

    @GetMapping("/{code}")
    public ResponseEntity<TestMember> getMemberByCode(@PathVariable Long code) {
        TestMember member = service.findByMemberCode(code);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(member);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTestMember() {
        // 테스트용 멤버 생성
        TestMember testMember = TestMember.builder()
                .name("테스트회원")
                .age(25)
                .birthday(LocalDate.of(1999, 3, 7))
                .build();

        // 여기서는 단순히 성공 메시지만 반환
        // 실제로는 저장 로직이 필요합니다
        return ResponseEntity.ok("테스트 멤버 생성 성공!");
    }
}
