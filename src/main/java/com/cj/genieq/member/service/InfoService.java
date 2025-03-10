package com.cj.genieq.member.service;

import com.cj.genieq.member.dto.response.MemberInfoResponseDto;

public interface InfoService {
    MemberInfoResponseDto getMemberInfo(Long memCode);
}
