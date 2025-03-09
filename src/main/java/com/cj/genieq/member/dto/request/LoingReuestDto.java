package com.cj.genieq.member.dto.request;

import lombok.*;
import org.hibernate.annotations.IdGeneratorType;

//단순한 로그인 데이터를 전달하기 때문에 @data를 안 쓰고, @getter/@setter를 쓴 거다.
@Getter
@Setter
public class LoingReuestDto {
    private String memEmail;
    private String memPassword;
}
