package com.cj.genieq.passage.controller;

import com.cj.genieq.member.dto.response.LoginMemberResponseDto;
import com.cj.genieq.passage.dto.request.PassageFavoriteRequestDto;
import com.cj.genieq.passage.dto.request.PassageInsertRequestDto;
import com.cj.genieq.passage.dto.request.PassageUpdateRequestDto;
import com.cj.genieq.passage.dto.response.PassageFavoriteResponseDto;
import com.cj.genieq.passage.dto.response.PassageSelectResponseDto;
import com.cj.genieq.passage.dto.response.PassagePreviewListDto;
import com.cj.genieq.passage.service.PassageService;
import jakarta.persistence.EntityNotFoundException;
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

    @GetMapping("/select/prevlist")
    public ResponseEntity<?> selectPrevList(HttpSession session) {
        try{
            LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

            if (loginMember == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }

            List<PassagePreviewListDto> previews = passageService.getPreviewList(loginMember.getMemberCode());

            // 지문 목록이 비어있는 경우 처리 (optional)
            if (previews.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("지문 목록이 없습니다.");
            }

            return ResponseEntity.ok(previews);
        } catch (EntityNotFoundException e) {
            // 예를 들어, 서비스에서 데이터가 없을 경우의 예외 처리
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("지문 정보가 존재하지 않습니다.");
        } catch (Exception e) {
            // 예기치 못한 예외 처리
            e.printStackTrace(); // 로그로 예외 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    // 지문 개별 조회
    @GetMapping("/select/{pasCode}")
    public ResponseEntity<?> selectPassage(@PathVariable Long pasCode) {
        try {
            // PassageService에서 지문 정보를 조회
            PassageSelectResponseDto passage = passageService.selectPassage(pasCode);

            // 지문이 존재하지 않으면 예외 처리
            if (passage == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("지문을 찾을 수 없습니다.");
            }

            return ResponseEntity.ok(passage);

        } catch (EntityNotFoundException e) {
            // 지문이 없을 경우 예외 처리
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("지문을 찾을 수 없습니다.");
        } catch (Exception e) {
            // 기타 예외 처리 (예기치 않은 오류)
            e.printStackTrace();  // 로깅용
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
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
}
