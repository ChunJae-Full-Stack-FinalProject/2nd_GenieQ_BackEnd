package com.cj.genieq.usage.controller;

import com.cj.genieq.member.dto.response.LoginMemberResponseDto;
import com.cj.genieq.usage.dto.response.UsageListResponseDto;
import com.cj.genieq.usage.service.UsageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/usag")
@RequiredArgsConstructor
public class UsageController {

    private final UsageService usageService;

    @GetMapping("/select/list")
    public ResponseEntity<?> selectList(
            @RequestParam("startDate")LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            HttpSession session){
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        List<UsageListResponseDto> usages = usageService.getUsageList(loginMember.getMemberCode(), startDate, endDate, page, size);
        return ResponseEntity.ok(usages);
    }
    
    // 이용권 추가 및 차감에 따른 이용 내역 저장
    // 서비스만 이용할 예정(해당 컨트롤러 사용 안함)
    @PostMapping("/insert/each")
    public ResponseEntity<?> insertEach(){
        // 이용권 추가 시 사용
        usageService.updateUsage(1L, 1, "이용권 차감");

        // 이용권 차감 시 사용
        usageService.updateUsage(1L, -50, "지문 생성");
        return ResponseEntity.ok("차감 성공");
    }
}
