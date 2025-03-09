package com.cj.genieq.usage.controller;

import com.cj.genieq.usage.dto.response.UsageListResponseDto;
import com.cj.genieq.usage.service.UsageService;
import lombok.RequiredArgsConstructor;
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
            @RequestParam("memCode") Long memCode,
            @RequestParam("startDate")LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size){

        List<UsageListResponseDto> usages = usageService.getUsageList(memCode, startDate, endDate, page, size);
        return ResponseEntity.ok(usages);
    }
}
