package com.cj.genieq.test.controller;

import com.cj.genieq.test.dto.TestMember;
import com.cj.genieq.test.service.TestMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
class TestMemberCommandLineRunner implements CommandLineRunner {

    private final TestMemberService service;

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);
        String name = "";

        while(!name.equals("q")) {
             System.out.print("DB에 저장시킬 이름을 입력해주세요 (종료: q): ");
            name = sc.next();
            sc.nextLine(); // 버퍼 비우기

            if(!name.equals("q")) {
                TestMember member = TestMember.builder().name(name).build();
                TestMember savedMember = service.saveMember(member);
                // System.out.println(name + " 저장 완료! 생성된 멤버 코드: " + savedMember.getMemberCode());
            }
        }
        sc.close();
        // System.out.println("CLI 테스트 종료.");
    }
}