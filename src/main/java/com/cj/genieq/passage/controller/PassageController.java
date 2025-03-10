package com.cj.genieq.passage.controller;

import com.cj.genieq.passage.dto.PassageContentDto;
import com.cj.genieq.passage.dto.request.PassageFavoriteRequestDto;
import com.cj.genieq.passage.dto.response.PassageFavoriteResponseDto;
import com.cj.genieq.passage.dto.response.PassageTitleListDto;
import com.cj.genieq.passage.service.PassageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pass")
@RequiredArgsConstructor
public class PassageController {

    private final PassageService passageService;

    @PostMapping("/insert/each")
    public ResponseEntity<?> insertEach(@RequestBody PassageContentDto passageDto) {
        PassageContentDto savedPassage = passageService.savePassage(passageDto);
        if (savedPassage != null) {
            return ResponseEntity.ok(savedPassage);
        } else {
            return ResponseEntity.badRequest().body("저장 실패");
        }
    }

    @GetMapping("/select/list")
    public ResponseEntity<?> getPaginatedPassages(
            @RequestParam("memCode") Long memCode,
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<PassageTitleListDto> passages= passageService.getPaginatedPassagesByTitle(memCode, keyword, page, size);
        return ResponseEntity.ok(passages);

    }

    @PatchMapping("/favo")
    public ResponseEntity<PassageFavoriteResponseDto> favoritePassage(@RequestBody PassageFavoriteRequestDto requestDto) {
        PassageFavoriteResponseDto responseDto = passageService.favoritePassage(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
