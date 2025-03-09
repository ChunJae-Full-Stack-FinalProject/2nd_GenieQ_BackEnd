package com.cj.genieq.passage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class PassageTitleListDto {
    private Long passageCode;
    private String passageTitle;
    private String subjectTitle;
    private LocalDate date;
    private Integer favorite;
}
