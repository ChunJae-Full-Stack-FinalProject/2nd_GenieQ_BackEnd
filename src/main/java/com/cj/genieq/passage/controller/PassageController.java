package com.cj.genieq.passage.controller;

import com.cj.genieq.member.dto.response.LoginMemberResponseDto;
import com.cj.genieq.passage.dto.request.PassageFavoriteRequestDto;
import com.cj.genieq.passage.dto.request.PassageInsertRequestDto;
import com.cj.genieq.passage.dto.request.PassageUpdateRequestDto;
import com.cj.genieq.passage.dto.request.PassageWithQuestionsRequestDto;
import com.cj.genieq.passage.dto.response.PassageFavoriteResponseDto;
import com.cj.genieq.passage.dto.response.PassageSelectResponseDto;
import com.cj.genieq.passage.dto.response.PassageTitleListDto;
import com.cj.genieq.passage.dto.response.PassageWithQuestionsResponseDto;
import com.cj.genieq.passage.service.PassageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pass")
@RequiredArgsConstructor
public class PassageController {

    private final PassageService passageService;

    @PostMapping("/insert/each")
    public ResponseEntity<?> insertEach(HttpSession session, @RequestBody PassageInsertRequestDto passageDto) {
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        
        // 지문 생성
        PassageSelectResponseDto savedPassage = passageService.savePassage(loginMember.getMemberCode(), passageDto);
                
        if (savedPassage != null) {
            return ResponseEntity.ok(savedPassage);
        } else {
            return ResponseEntity.badRequest().body("저장 실패");
        }
    }

    @PatchMapping("/update/each")
    public ResponseEntity<?> updatePassage(@RequestBody PassageUpdateRequestDto passageDto) {
        try {
            // 지문 수정 및 업데이트된 지문 정보 반환
            PassageSelectResponseDto updatedPassage = passageService.updatePassage(passageDto);
            return ResponseEntity.ok(updatedPassage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("지문 수정 실패");
        }
    }

//    @GetMapping("/select/list")
//    public ResponseEntity<?> getPaginatedPassages(
//            @RequestParam("memCode") Long memCode,
//            @RequestParam("keyword") String keyword,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        List<PassageTitleListDto> passages= passageService.getPaginatedPassagesByTitle(memCode, keyword, page, size);
//        return ResponseEntity.ok(passages);
//
//    }

    @PatchMapping("/favo")
    public ResponseEntity<PassageFavoriteResponseDto> favoritePassage(@RequestBody PassageFavoriteRequestDto requestDto) {
        PassageFavoriteResponseDto responseDto = passageService.favoritePassage(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/ques/insert/each")
    public ResponseEntity<?> insertEach(HttpSession session, @RequestBody PassageWithQuestionsRequestDto passageWithQuestionDto) {
        // 세션에서 로그인 사용자 정보를 가져옴
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        // 로그인 상태 확인
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 지문과 문항 생성 및 저장
        PassageWithQuestionsResponseDto savedPassageWithQuestions = passageService.savePassageWithQuestions(loginMember.getMemberCode(), passageWithQuestionDto);

        // 저장된 지문과 문항 반환
        if (savedPassageWithQuestions != null) {
            return ResponseEntity.ok(savedPassageWithQuestions);
        } else {
            return ResponseEntity.badRequest().body("저장 실패");
        }
    }

}
