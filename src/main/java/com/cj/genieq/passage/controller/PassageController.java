package com.cj.genieq.passage.controller;

import com.cj.genieq.member.dto.response.LoginMemberResponseDto;
import com.cj.genieq.passage.dto.request.*;
import com.cj.genieq.passage.dto.response.*;
import com.cj.genieq.passage.service.PassageService;
import com.cj.genieq.passage.service.PdfService;
import com.cj.genieq.passage.service.WordService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    private final PdfService pdfService;
    private final WordService wordService;

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

    // 자료실 메인화면 리스트(즐겨찾기+최근 작업)
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

    // 즐겨찾기 리스트
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

    // 최근 작업 내역 리스트
    @GetMapping("/select/recelist")
    public ResponseEntity<?> selectRecent(HttpSession session) {
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        // 로그인 상태 확인
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        List<PassageStorageEachResponseDto> recents = passageService.selectRecentList(loginMember.getMemberCode());

        return ResponseEntity.ok(recents);
    }

    // 지문 삭제
    @PutMapping("/remove/each")
    public ResponseEntity<?> removePassage(@RequestBody PassageDeleteRequestDto requestDto) {
        if (requestDto.getPasCodeList() == null || requestDto.getPasCodeList().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제할 대상이 없습니다.");
        }

        try {
            boolean result = passageService.deletePassage(requestDto);
            if (result) {
                return ResponseEntity.ok("삭제 완료");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제에 실패했습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버에서 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 작업명(지문 이름) 변경
    @PutMapping("/update/title")
    public ResponseEntity<?> updatePassageTitle(@RequestBody PassageUpdateTitleRequestDto requestDto) {
        if (requestDto.getPasCode() == null || requestDto.getTitle() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("필수 값이 없습니다.");
        }

        try {
            boolean result = passageService.updatePassageTitle(requestDto);
            if (result) {
                return ResponseEntity.ok("지문 제목이 수정되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("기존 제목과 동일합니다.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버에서 오류가 발생했습니다: " + e.getMessage());
        }
    }

//    @GetMapping("/export/each")
//    public ResponseEntity<byte[]> generatePdf(@RequestParam String content) {
//
//
//        byte[] pdfData = pdfService.createPdf(content);
//
//        // PDF 다운로드 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=document.pdf");
//        headers.add("Content-Type", "application/pdf");
//
//        return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
//    }

    @PostMapping("/export/each")
    public ResponseEntity<byte[]> generatePdf(@RequestBody String jsonData) {
        byte[] pdfData = pdfService.createPdfFromJson(jsonData);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=document.pdf");
        headers.add("Content-Type", "application/pdf");

        return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
    }

    @GetMapping("/generate/{pasCode}")
    public ResponseEntity<byte[]> generateWord(@PathVariable("pasCode") Long pasCode) {
        PassageWithQuestionsRequestDto responseDto = passageService.getPassageWithQuestions(pasCode);
        byte[] wordData = wordService.createWordFromDto(responseDto);

        HttpHeaders headers = new HttpHeaders();
        // ✅ 파일 이름 명확히 설정
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document.docx\"");
        // ✅ 응답 타입 명확히 설정 (바이너리 인식)
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        return new ResponseEntity<>(wordData, headers, HttpStatus.OK);
    }
}
