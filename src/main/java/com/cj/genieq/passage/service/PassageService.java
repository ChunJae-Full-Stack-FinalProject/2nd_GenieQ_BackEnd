package com.cj.genieq.passage.service;

import com.cj.genieq.passage.dto.request.PassageFavoriteRequestDto;
import com.cj.genieq.passage.dto.request.PassageInsertRequestDto;
import com.cj.genieq.passage.dto.request.PassageUpdateRequestDto;
import com.cj.genieq.passage.dto.request.PassageWithQuestionsRequestDto;
import com.cj.genieq.passage.dto.response.*;

import java.util.List;

public interface PassageService {
    PassageSelectResponseDto savePassage(Long memCode, PassageInsertRequestDto passageDto);
    PassageFavoriteResponseDto favoritePassage(PassageFavoriteRequestDto requestDto);
    PassageSelectResponseDto updatePassage(PassageUpdateRequestDto passageDto);
    List<PassagePreviewListDto> getPreviewList(Long memCode);
    PassageSelectResponseDto selectPassage(Long pasCode);

    Long savePassageWithQuestions(Long memCode, PassageWithQuestionsRequestDto requestDto);
    PassageWithQuestionsRequestDto getPassageWithQuestions(Long pasCode);
    List<PassageStorageEachResponseDto> selectPassageListInStorage(Long memCode, Integer isFavorite, Integer rownum);
    List<PassageStorageEachResponseDto> selectFavoriteList(Long memCode);
    List<PassageStorageEachResponseDto> selectRecentList(Long memCode);
}
