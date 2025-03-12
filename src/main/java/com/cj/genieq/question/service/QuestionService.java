package com.cj.genieq.question.service;

import com.cj.genieq.passage.entity.PassageEntity;
import com.cj.genieq.question.dto.request.QuestionInsertRequestDto;

import java.util.List;

public interface QuestionService {
    void saveQuestions(PassageEntity savedPassage, List<QuestionInsertRequestDto> questions);

}