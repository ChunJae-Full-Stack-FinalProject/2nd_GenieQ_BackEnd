package com.cj.genieq.question.service;

import com.cj.genieq.question.dto.QuestionDto;
import com.cj.genieq.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {


    @Override
    public QuestionDto saveQuestion(QuestionDto question) {
        return null;
    }
}
