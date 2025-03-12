package com.cj.genieq.question.service;

import com.cj.genieq.passage.entity.PassageEntity;
import com.cj.genieq.question.dto.QuestionDto;
import com.cj.genieq.question.dto.request.QuestionInsertRequestDto;
import com.cj.genieq.question.entity.QuestionEntity;
import com.cj.genieq.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    // ✅ 문항 저장 로직 이동
    public void saveQuestions(PassageEntity savedPassage, List<QuestionInsertRequestDto> questions) {
        List<QuestionEntity> questionEntities = questions.stream()
                .map(q -> QuestionEntity.builder()
                        .queQuery(q.getQueQuery())
                        .queOption(String.join(",", q.getQueOption())) // JSON → String 변환 후 저장
                        .queAnswer(q.getQueAnswer())
                        .passage(savedPassage) // 지문 코드 매핑
                        .build())
                .collect(Collectors.toList());

        // 한 번에 저장
        questionRepository.saveAll(questionEntities);
    }

}
