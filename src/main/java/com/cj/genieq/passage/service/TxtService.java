package com.cj.genieq.passage.service;

import com.cj.genieq.passage.dto.response.PassageWithQuestionsResponseDto;
import com.cj.genieq.question.dto.response.QuestionSelectResponseDto;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class TxtService {

    public byte[] createTxtFromDto(PassageWithQuestionsResponseDto dto) {
        StringBuilder sb = new StringBuilder();

        // ✅ 제목 작성
        sb.append("제목: ").append(dto.getTitle()).append("\n");
        sb.append("유형: ").append(dto.getType()).append("\n");
        sb.append("키워드: ").append(dto.getKeyword()).append("\n");
        sb.append("\n");

        // ✅ 본문 작성
        sb.append("[내용]").append("\n");
        sb.append(dto.getContent()).append("\n\n");

        // ✅ 요지 작성
        sb.append("[요지]").append("\n");
        sb.append(dto.getGist()).append("\n\n");

        // ✅ 문제 작성
        if (dto.getQuestions() != null && !dto.getQuestions().isEmpty()) {
            sb.append("[문제]").append("\n");
            for (QuestionSelectResponseDto question : dto.getQuestions()) {
                sb.append("Q: ").append(question.getQueQuery()).append("\n");
                for (String option : question.getQueOption()) {
                    sb.append(" - ").append(option).append("\n");
                }
                sb.append("정답: ").append(question.getQueAnswer()).append("\n\n");
            }
        }

        // ✅ 문자열을 바이트 배열로 변환 (UTF-8 인코딩)
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}