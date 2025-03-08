package com.cj.genieq.passage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class PassageRequestDto {
    private Long passCode;
    private String type;
    private String keyword;
    private String title;
    private String content;
    private String gist;

    private Long memCode;
}
