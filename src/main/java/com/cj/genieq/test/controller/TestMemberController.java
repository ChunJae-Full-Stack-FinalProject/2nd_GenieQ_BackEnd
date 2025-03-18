package com.cj.genieq.test.controller;

import com.cj.genieq.test.dto.TestMember;
import com.cj.genieq.test.service.TestMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestMemberController {

    private final TestMemberService service;

    // (수정) 더 구체적인 경로를 상위에 배치하여 패턴 충돌 방지
    @PostMapping("/generate-passage")
    public ResponseEntity<Map<String, Object>> generatePassage(@RequestBody Map<String, Object> requestBody) {
//        System.out.println("[DummyApiController] 지문 생성 요청 받음: " + requestBody);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String typePassage = (String) requestBody.getOrDefault("type_passage", "[type_passage(지문 유형) 값을 받아오지 못했습니다.]");
        List<String> keyword = (List<String>) requestBody.getOrDefault("keyword", "[keyword(지문 제재) 값을 받아오지 못했습니다.]");
        String currentTime = now.format(formatter);

        Map<String, Object> response = new HashMap<>();

        // 기본 지문 내용
        response.put("generated_passage",
                "이것은 " + typePassage + " 분야의 " + keyword.toString() + "에 대한 더미 지문입니다. " +
                        "요청 시간: " + currentTime + ". \n\\n\\\n" +
                        "이 줄은 개행 기호 정규 표현식 '슬래시 n'가 문장 끝에 두개 붙어있습니다.\n\n" +
                        "이 줄은 개행 기호 정규 표현식 '슬래시 n'가 문장 끝에 두개 붙어있습니다.\n\n" +
                        "이 줄은 개행 기호 정규 표현식 '슬래시 n'가 문장 끝에 두개 붙어있습니다.\n\n" +
                        "이 줄은 개행 기호 정규 표현식 '슬래시 슬래시 n'가 문장 끝에 두개 붙어있습니다.\\n\\n" +
                        "이 줄은 개행 기호 정규 표현식 '슬래시 슬래시 n'가 문장 끝에 두개 붙어있습니다.\\n\\n" +
                        "이 줄은 개행 기호 정규 표현식 '슬래시 슬래시 n'가 문장 끝에 두개 붙어있습니다.\\n\\n" +
                        "이 줄은 개행 기호 정규 표현식 '슬래시 슬래스 슬래시 n'가 문장 끝에 두개 붙어있습니다.\\\n\\\n"+
                        "이 줄은 개행 기호 정규 표현식 '슬래시 슬래스 슬래시 n'가 문장 끝에 두개 붙어있습니다.\\\n\\\n"+
                        "이 줄은 개행 기호 정규 표현식 '슬래시 슬래스 슬래시 n'가 문장 끝에 두개 붙어있습니다.\\\n\\\n"
        );

        // 핵심 논점은 문자열로 제공
        response.put("generated_core_point",
                typePassage + " 분야에서 " + keyword.toString() + "의 핵심 논점입니다. 생성 시간: " + currentTime
        );

//        System.out.println("[DummyApiController] 지문 생성 응답 전송: " + response);
        return ResponseEntity.ok(response);
    }

    // (수정) 더 구체적인 경로를 상위에 배치하여 패턴 충돌 방지
    @PostMapping("/generate-question")
    public ResponseEntity<Map<String, Object>> generateQuestion(@RequestBody Map<String, Object> requestBody) {
//        System.out.println("[DummyApiController] 문항 생성 요청 받음: " + requestBody);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String customPassage = (String) requestBody.getOrDefault("custom_passage", "[입력된 지문을 불러오지 못했습니다.]");
        String typeQuestion = (String) requestBody.getOrDefault("type_question", "[입력된 typeQuestion 을 가져오지 못했습니다.]");
        String typeQuestionDetail = (String) requestBody.getOrDefault("type_question_detail", "[입력된 typeQuestionDetail을 가져오지 못했습니다]");
        String questionExample = (String) requestBody.getOrDefault("question_example", "[입력된 question_example 를 가져오지 못했습니다]");
        String currentTime = now.format(formatter);

        Map<String, Object> response = new HashMap<>();

        // 문항 생성
        response.put("generated_question", questionExample);

        // 선택지 배열 생성
        String[] options = {
                "첫 번째 선택지 - " + typeQuestion + " : " + currentTime,
                "두 번째 선택지 - " + typeQuestionDetail + " : " + currentTime,
                "세 번째 선택지 - " + currentTime,
                "네 번째 선택지",
                "다섯 번째 선택지"
        };
        response.put("generated_option", options);

        // 정답과 설명
        response.put("generated_answer", "②");
        response.put("generated_description", "정답은 두 번째 선택지입니다. 이유: " + typeQuestion + ", " + typeQuestionDetail + ", 시간: " + currentTime);

//        System.out.println("[DummyApiController] 문항 생성 응답 전송: " + response);
        return ResponseEntity.ok(response);
    }

    // (추가) 타입/키워드 정보 API 추가
    @PostMapping("/get-type-keyword")
    public ResponseEntity<Map<String, Object>> getTypeKeyword(@RequestBody Map<String, Object> requestBody) {
        // System.out.println("[DummyApiController] 타입/키워드 정보 요청 받음: " + requestBody);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = now.format(formatter);

        // 더미 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("type_passage", "인문");
        response.put("keyword", "키워드_" + currentTime.substring(11).replace(":", ""));

        // System.out.println("[DummyApiController] 타입/키워드 정보 응답 전송: " + response);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TestMember> createTestMember(@RequestBody TestMember member) {
        TestMember savedMember = service.saveMember(member);
        return ResponseEntity.ok(savedMember); // 저장된 회원 정보를 응답으로 반환 (생성된 ID 포함)
    }
}