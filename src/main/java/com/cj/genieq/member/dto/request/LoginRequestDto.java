package com.cj.genieq.member.dto.request;

import lombok.*;

@Getter
@Setter
public class LoginRequestDto {
    private String memEmail;
    private String memPassword;
}
