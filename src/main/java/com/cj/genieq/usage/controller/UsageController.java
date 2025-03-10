package com.cj.genieq.usage.controller;

import com.cj.genieq.member.dto.response.LoginMemberResponseDto;
import com.cj.genieq.usage.dto.response.UsageListResponseDto;
import com.cj.genieq.usage.service.UsageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
