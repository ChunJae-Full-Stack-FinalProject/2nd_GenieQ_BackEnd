package com.cj.genieq.test.controller;

import com.cj.genieq.test.model.dto.TestMember;
import com.cj.genieq.test.model.service.TestMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/test")
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

    @PostMapping
    public ResponseEntity<TestMember> createTestMember(@RequestBody TestMember member) {
        TestMember savedMember = service.saveMember(member);
        return ResponseEntity.ok(savedMember); // 저장된 회원 정보를 응답으로 반환 (생성된 ID 포함)
    }
}
