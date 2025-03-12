package com.cj.genieq.question.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

//문항 저장에 사용
public class QuestionInsertRequestDto {
    private Long queCode;
    private String queQuery; //질문
    private List<String> queOption; //보기
    private String queAnswer; //해설
}
