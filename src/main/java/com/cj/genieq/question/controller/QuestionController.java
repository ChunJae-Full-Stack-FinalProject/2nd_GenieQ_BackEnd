package com.cj.genieq.question.controller;

import com.cj.genieq.question.dto.QuestionDto;
import com.cj.genieq.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ques")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/insert/each")
    public ResponseEntity<?> insertQuestion(@RequestBody QuestionDto questionDto) {
        QuestionDto savedQuestion = questionService.saveQuestion(questionDto);
        if(savedQuestion != null) {
            return ResponseEntity.ok().body(savedQuestion);
        }
        else {
            return ResponseEntity.badRequest().body("저장 실패");
        }
    }
}
