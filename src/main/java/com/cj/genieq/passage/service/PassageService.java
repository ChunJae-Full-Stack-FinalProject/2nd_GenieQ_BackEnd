package com.cj.genieq.passage.service;

import com.cj.genieq.passage.dto.request.PassageFavoriteRequestDto;
import com.cj.genieq.passage.dto.request.PassageInsertRequestDto;
import com.cj.genieq.passage.dto.request.PassageUpdateRequestDto;
import com.cj.genieq.passage.dto.request.PassageWithQuestionsRequestDto;
import com.cj.genieq.passage.dto.response.PassageFavoriteResponseDto;
import com.cj.genieq.passage.dto.response.PassageSelectResponseDto;
import com.cj.genieq.passage.dto.response.PassagePreviewListDto;
import com.cj.genieq.passage.dto.response.PassageWithQuestionsResponseDto;

import java.util.List;

public interface PassageService {
    PassageSelectResponseDto savePassage(Long memCode, PassageInsertRequestDto passageDto);
    //List<PassageTitleListDto> getPaginatedPassagesByTitle(Long memCode, String title, int page, int size);
    PassageFavoriteResponseDto favoritePassage(PassageFavoriteRequestDto requestDto);
    PassageSelectResponseDto updatePassage(PassageUpdateRequestDto passageDto);
    List<PassagePreviewListDto> getPreviewList(Long memCode);
    PassageSelectResponseDto selectPassage(Long pasCode);

    PassageWithQuestionsResponseDto savePassageWithQuestions(Long memCode, PassageWithQuestionsRequestDto requestDto);
    PassageWithQuestionsResponseDto getPassageWithQuestions(Long pasCode);
    public Long updatePassage(Long pasCode, PassageWithQuestionsRequestDto requestDto);
}
