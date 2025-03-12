package com.cj.genieq.passage.controller;

import com.cj.genieq.member.dto.response.LoginMemberResponseDto;
import com.cj.genieq.passage.dto.request.PassageFavoriteRequestDto;
import com.cj.genieq.passage.dto.request.PassageInsertRequestDto;
import com.cj.genieq.passage.dto.request.PassageUpdateRequestDto;
import com.cj.genieq.passage.dto.request.PassageWithQuestionsRequestDto;
import com.cj.genieq.passage.dto.response.*;
import com.cj.genieq.passage.service.PassageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataAccessException;

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

    @PatchMapping("/favo")
    public ResponseEntity<PassageFavoriteResponseDto> favoritePassage(@RequestBody PassageFavoriteRequestDto requestDto) {
        PassageFavoriteResponseDto responseDto = passageService.favoritePassage(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/ques/insert/each")
    public ResponseEntity<?> savePassage(HttpSession session, @RequestBody PassageWithQuestionsRequestDto requestDto) {
        // 세션에서 로그인 사용자 정보를 가져옴
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        // 로그인 상태 확인
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Long pasCode = passageService.savePassageWithQuestions(loginMember.getMemberCode(), requestDto);
        return ResponseEntity.ok(pasCode);
    }

    // 지문 + 문항 조회
    @GetMapping("/{pasCode}")
    public ResponseEntity<PassageWithQuestionsRequestDto> getPassage(@PathVariable Long pasCode) {
        PassageWithQuestionsRequestDto responseDto = passageService.getPassageWithQuestions(pasCode);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/select/list")
    public ResponseEntity<?> selectList(HttpSession session) {
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        // 로그인 상태 확인
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try {
            List<PassageStorageEachResponseDto> favorites = passageService.selectPassageListInStorage(loginMember.getMemberCode(), 1, 5);
            List<PassageStorageEachResponseDto> recent = passageService.selectPassageListInStorage(loginMember.getMemberCode(), 0, 8);

            PassageStorageMainResponseDto responseDto = PassageStorageMainResponseDto.builder()
                    .favorites(favorites)
                    .recent(recent)
                    .build();

            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            // 잘못된 파라미터 값 예외 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다: " + e.getMessage());
        } catch (DataAccessException e) {
            // DB 오류 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터베이스 오류가 발생했습니다.");
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 문제가 발생했습니다.");
        }
    }

    @GetMapping("/select/favolist")
    public ResponseEntity<?> selectFavoList(HttpSession session) {
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        // 로그인 상태 확인
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        List<PassageStorageEachResponseDto> favorites = passageService.selectFavoriteList(loginMember.getMemberCode());

        return ResponseEntity.ok(favorites);
    }

}
