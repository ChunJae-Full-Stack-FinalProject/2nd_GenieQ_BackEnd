package com.cj.genieq.passage.service;

import com.cj.genieq.passage.dto.PassageContentDto;
import com.cj.genieq.passage.dto.response.PassageTitleListDto;

import java.util.List;

public interface PassageService {
    PassageContentDto savePassage(PassageContentDto passage);
    List<PassageTitleListDto> getPaginatedPassagesByTitle(Long memCode, String title, int page, int size);
}
