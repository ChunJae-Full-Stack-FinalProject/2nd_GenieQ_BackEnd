package com.cj.genieq.passage.controller;

import com.cj.genieq.passage.dto.Passage;
import com.cj.genieq.passage.dto.PassageRequestDto;
import com.cj.genieq.passage.service.PassageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pass")
@RequiredArgsConstructor
public class PassageController {

    private final PassageService passageService;

    @PostMapping("/insert/each")
    public ResponseEntity<?> insertEach(@RequestBody PassageRequestDto passageDto) {
        PassageRequestDto savedPassage = passageService.savePassage(passageDto);
        if (savedPassage != null) {
            return ResponseEntity.ok(savedPassage);
        } else {
            return ResponseEntity.badRequest().body("저장 실패");
        }
    }
}
