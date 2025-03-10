package com.cj.genieq.notice.service;

import com.cj.genieq.notice.dto.response.NoticeListResponseDto;

import java.util.List;

public interface NoticeService {
    List<NoticeListResponseDto> getNoticeList(String type, int page, int size);
}
