package com.cj.genieq.member.dto.request;

import lombok.*;
import org.hibernate.annotations.IdGeneratorType;

@Getter
@Setter
public class LoingReuestDto {
    private String memEmail;
    private String memPassword;
}
