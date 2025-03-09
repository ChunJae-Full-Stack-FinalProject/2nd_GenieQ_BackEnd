package com.cj.genieq.usage.service;

import com.cj.genieq.usage.dto.response.UsageListResponseDto;
import com.cj.genieq.usage.entity.UsageEntity;
import com.cj.genieq.usage.repository.UsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsageServiceImpl implements UsageService{
    private final UsageRepository usageRepository;

    public List<UsageListResponseDto> getUsageList(
            Long memCode, LocalDate startDate, LocalDate endDate, int page, int size) {

        int startRow = page * size;
        int endRow = startRow + size;

        List<UsageEntity> usageEntities = usageRepository.findByMemCodeAndDateRange(
                memCode, startDate, endDate, startRow, endRow);

        return usageEntities.stream()
                .map(usage -> UsageListResponseDto.builder()
                        .usaCode(usage.getUsaCode())
                        .usaType(usage.getUsaType()) // 한글 값 반환
                        .usaCount(usage.getUsaCount())
                        .usaBalance(usage.getUsaBalance())
                        .usaDate(usage.getUsaDate())
                        .build())
                .collect(Collectors.toList());
    }
}
