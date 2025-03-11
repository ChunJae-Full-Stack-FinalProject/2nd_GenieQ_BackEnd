package com.cj.genieq.passage.service;

import com.cj.genieq.member.entity.MemberEntity;
import com.cj.genieq.member.repository.MemberRepository;
import com.cj.genieq.passage.dto.request.PassageFavoriteRequestDto;
import com.cj.genieq.passage.dto.request.PassageInsertRequestDto;
import com.cj.genieq.passage.dto.response.PassageFavoriteResponseDto;
import com.cj.genieq.passage.dto.response.PassageSelectResponseDto;
import com.cj.genieq.passage.dto.response.PassageTitleListDto;
import com.cj.genieq.passage.entity.PassageEntity;
import com.cj.genieq.passage.repository.PassageRepository;
import com.cj.genieq.usage.repository.UsageRepository;
import com.cj.genieq.usage.service.UsageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassageServiceImpl implements PassageService {

    private final PassageRepository passageRepository;
    private final MemberRepository memberRepository;
    private final UsageRepository usageRepository;
    private final UsageService usageService;

    @Override
    @Transactional
    public PassageSelectResponseDto savePassage(Long memCode, PassageInsertRequestDto passageDto) {
        try {
            // 회원 조회
            MemberEntity member = memberRepository.findById(memCode)
                    .orElseThrow(() -> new EntityNotFoundException("Member not found"));
            
             // 제목 중복 처리
            String title = generateTitle(passageDto.getTitle());

            // Passage 엔티티 생성 및 저장
            PassageEntity passage = PassageEntity.builder()
                    .pasType(passageDto.getType())
                    .keyword(passageDto.getKeyword())
                    .title(title)
                    .content(passageDto.getContent())
                    .gist(passageDto.getGist())
                    .isGenerated(passageDto.getIsGenerated())
                    .member(member)
                    .build();

            PassageEntity savedPassage = passageRepository.save(passage);

            usageService.updateUsage(memCode, -1, "지문 생성");

            PassageSelectResponseDto selectedPassage =  PassageSelectResponseDto.builder()
                        .pasCode(savedPassage.getPasCode())
                        .title(savedPassage.getTitle())
                        .type(savedPassage.getPasType())
                        .keyword(savedPassage.getKeyword())
                        .content(savedPassage.getContent())
                        .gist(savedPassage.getGist())
                        .build();

            return selectedPassage;
        } catch (EntityNotFoundException e) {
            // 회원이 없으면 EntityNotFoundException 예외 처리
            throw new EntityNotFoundException("지문 저장 실패: " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            // 데이터 무결성 오류 처리
            throw new DataIntegrityViolationException("데이터 무결성 위반: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();  // 예외 로깅
            return null;
        }
    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<PassageTitleListDto> getPaginatedPassagesByTitle(Long memCode, String title, int page, int size) {
//        String searchKeyword = "%" + title + "%";
//
//        int startRow = page * size;
//        int endRow = startRow + size;
//
//        List<PassageEntity> result = passageRepository
//                .findByMemCodeAndKeyword(memCode, searchKeyword, startRow, endRow);
//
//        return result.stream()
//                .map(passage -> PassageTitleListDto.builder()
//                        .passageCode(passage.getPasCode())
//                        .passageTitle(passage.getTitle())
//                        .subjectKeyword(passage.getSubject().getSubKeyword())
//                        .date(passage.getDate())
//                        .favorite(passage.getIsFavorite())
//                        .build())
//                .toList();
//    }

    @Override
    @Transactional
    public PassageFavoriteResponseDto favoritePassage(PassageFavoriteRequestDto requestDto){
        PassageEntity passage = passageRepository.findById(requestDto.getPasCode())
                .orElseThrow(() -> new IllegalArgumentException("지문이 존재하지 않습니다."));

        //상태
        passage.setIsFavorite(passage.getIsFavorite() == 1 ? 0 : 1); //현재 값이 1이면 0(즐겨찾기 해제) / 0이면 1(즐겨찾기 추가)
        passageRepository.save(passage);

        return  PassageFavoriteResponseDto.builder()
                .pasCode(passage.getPasCode())
                .isFavorite(passage.getIsFavorite())
                .build();
    }
    
    // 제목 중복 처리 메소드
    private String generateTitle(String title){
        // 제목 중복 처리
        String originalTitle = title;
        int counter = 1;

        // 제목 중복 체크
        while (passageRepository.existsByTitle(title)) {
            // 제목이 이미 존재하면 (1), (2), (3)... 형식으로 수정
            title = originalTitle + "(" + counter + ")";
            counter++;
        }

        return title;
    }

}
