package com.cj.genieq.passage.controller;

import com.cj.genieq.member.dto.response.LoginMemberResponseDto;
import com.cj.genieq.passage.dto.request.*;
import com.cj.genieq.passage.dto.response.*;
import com.cj.genieq.passage.service.PassageService;
import com.cj.genieq.passage.service.PdfService;
import com.cj.genieq.passage.service.TxtService;
import com.cj.genieq.passage.service.WordService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataAccessException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/pass")
@RequiredArgsConstructor
public class PassageController {

    private final PassageService passageService;
    private final PdfService pdfService;
    private final WordService wordService;
    private final TxtService txtService;

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
        try {
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


    // 지문 + 문항 저장
    @PostMapping("/ques/insert/each")
    public ResponseEntity<?> savePassage(HttpSession session, @RequestBody PassageWithQuestionsRequestDto requestDto) {
        // 세션에서 로그인 사용자 정보를 가져옴
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        // 로그인 상태 확인
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        PassageWithQuestionsResponseDto responseDto = passageService.savePassageWithQuestions(loginMember.getMemberCode(), requestDto);

        return ResponseEntity.ok(responseDto);
    }

    // 지문 + 문항 조회
    @GetMapping("/ques/select/{pasCode}")
    public ResponseEntity<PassageWithQuestionsResponseDto> getPassage(@PathVariable Long pasCode) {
        PassageWithQuestionsResponseDto responseDto = passageService.getPassageWithQuestions(pasCode);
        return ResponseEntity.ok(responseDto);
    }

    // 지문 + 문항 수정
    @PutMapping("/ques/update/{pasCode}")
    public ResponseEntity<PassageWithQuestionsResponseDto> updatePassage(HttpSession session,
            @PathVariable Long pasCode,
            @RequestBody PassageWithQuestionsRequestDto requestDto) {
        // 세션에서 로그인 사용자 정보를 가져옴
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        PassageWithQuestionsResponseDto updatedPassage = passageService.updatePassage(loginMember.getMemberCode(), pasCode, requestDto);
        return ResponseEntity.ok(updatedPassage);

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

    @GetMapping("/select/count/recent")
    public ResponseEntity<?> countRecentChange(HttpSession session){
        LoginMemberResponseDto loginMember = (LoginMemberResponseDto) session.getAttribute("LOGIN_USER");

        // 로그인 상태 확인
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        int numberOfRecentChange = passageService.countRecentChange(loginMember.getMemberCode());

        return ResponseEntity.ok(numberOfRecentChange);
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

    // 파일 추출 (type: pdf/word/txt)
    @GetMapping("/export/each/{pasCode}")
    public ResponseEntity<byte[]> generateFile(@PathVariable("pasCode") Long pasCode, @RequestParam("type") String type) {
        try {
            // pasCode 유효성 검사 추가
            if (pasCode == null || pasCode <= 0) {
                throw new IllegalArgumentException("유효하지 않은 pasCode입니다 : " + pasCode);
            }
            PassageWithQuestionsResponseDto responseDto = passageService.getPassageWithQuestions(pasCode);

            // 응답 데이터 유효성 검증
            if (responseDto == null) {
                throw new IllegalArgumentException("해당 pasCode에 대한 데이터를 찾을 수 없습니다 : " + pasCode);
            }

            String fileName = responseDto.getTitle().trim();
            byte[] result = generateFile(responseDto, type);

            HttpHeaders headers = createHeaders(fileName, type);

            return new ResponseEntity<>(result, headers, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // 에러 로그 기록
            System.err.println("지문을 찾을 수 없음 : " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // 에러 로그 기록
            System.err.println("파일 생성 오류 : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // 파일 생성
    private byte[] generateFile(PassageWithQuestionsResponseDto dto, String type) {
        return switch (type.toLowerCase()) {
            case "pdf" -> pdfService.createPdfFromDto(dto);
            case "word" -> wordService.createWordFromDto(dto);
            case "txt" -> txtService.createTxtFromDto(dto);
            default -> throw new IllegalArgumentException("Unsupported file type: " + type);
        };
    }

    // 파일 추출을 위한 httpheader 생성
    private HttpHeaders createHeaders(String fileName, String type) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();

        String extension;
        String contentType;

        switch (type.toLowerCase()) {
            case "pdf" -> {
                extension = "pdf";
                contentType = "application/pdf";
            }
            case "word" -> {
                extension = "docx";
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            }
            case "txt" -> {
                extension = "txt";
                contentType = "text/plain; charset=UTF-8";
            }
            default -> throw new IllegalArgumentException("Unsupported file type: " + type);
        }

        // 파일 이름을 UTF-8로 URL 인코딩
        String encodedFileName = URLEncoder.encode(fileName + "." + extension, "UTF-8").replace("+", "%20"); // 공백을 `%20`으로 변환

        headers.setContentDispositionFormData("attachment", encodedFileName);
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        headers.add("Access-Control-Expose-Headers", "Content-Disposition");

        return headers;
    }
}
