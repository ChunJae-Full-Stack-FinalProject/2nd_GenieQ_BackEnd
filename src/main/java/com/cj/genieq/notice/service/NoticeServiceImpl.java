package com.cj.genieq.notice.service;

import com.cj.genieq.notice.dto.response.NoticeListResponseDto;
import com.cj.genieq.notice.entity.NoticeEntity;
import com.cj.genieq.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public List<NoticeListResponseDto> getNoticeList(String type, int page, int size) {

        int startRow = page * size;
        int endRow = startRow + size;

        List<NoticeEntity> entities = noticeRepository.selectByType(type, startRow, endRow);

        List<NoticeListResponseDto> result = entities.stream()
                .map(entity->NoticeListResponseDto.builder()
                        .notCode(entity.getNotCode())
                        .type(entity.getType())
                        .title(entity.getTitle())
                        .date(entity.getDate())
                        .build())
                .toList();

        return result;
    }
}
