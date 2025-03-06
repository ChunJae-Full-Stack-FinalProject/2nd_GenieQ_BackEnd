package com.cj.genieq.test.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestMember {
    private Long memberCode;
    @NotEmpty
    @Size(min = 4, max = 20, message="네글자 이상 입력하세요.")
    private String name;
    private Integer age;
    private LocalDate birthday;
}
