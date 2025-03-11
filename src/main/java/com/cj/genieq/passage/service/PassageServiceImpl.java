package com.cj.genieq.passage.service;

import com.cj.genieq.member.entity.MemberEntity;
import com.cj.genieq.member.repository.MemberRepository;
import com.cj.genieq.passage.dto.request.PassageFavoriteRequestDto;
import com.cj.genieq.passage.dto.request.PassageInsertRequestDto;
import com.cj.genieq.passage.dto.request.PassageUpdateRequestDto;
import com.cj.genieq.passage.dto.request.PassageWithQuestionsRequestDto;
import com.cj.genieq.passage.dto.response.PassageFavoriteResponseDto;
import com.cj.genieq.passage.dto.response.PassagePreviewListDto;
import com.cj.genieq.passage.dto.response.PassageSelectResponseDto;
import com.cj.genieq.passage.dto.response.PassageTitleListDto;
import com.cj.genieq.passage.dto.response.PassageWithQuestionsResponseDto;
import com.cj.genieq.passage.entity.PassageEntity;
import com.cj.genieq.passage.repository.PassageRepository;
import com.cj.genieq.question.dto.request.QuestionInsertRequestDto;
import com.cj.genieq.question.dto.response.QuestionSelectResponseDto;
import com.cj.genieq.question.entity.QuestionEntity;
import com.cj.genieq.usage.repository.UsageRepository;
import com.cj.genieq.usage.service.UsageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassageServiceImpl implements PassageService {

    private final PassageRepository passageRepository;
    private final MemberRepository memberRepository;
    private final UsageService usageService;

    // 지문 저장
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
                    .date(LocalDateTime.now())
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

    // 지문 수정
    @Override
    @Transactional
    public PassageSelectResponseDto updatePassage(PassageUpdateRequestDto passageDto) {
        // 1. 기존 지문 조회
        PassageEntity passage = passageRepository.findById(passageDto.getPasCode())
                .orElseThrow(() -> new EntityNotFoundException("지문이 존재하지 않습니다."));

        // 제목 중복 처리
        String title = generateTitle(passageDto.getTitle());

        // 2. 기존 지문 정보 수정
        passage.setTitle(title);
        passage.setContent(passageDto.getContent());
        passage.setDate(LocalDateTime.now());

        // 3. 지문 수정 후 저장
        PassageEntity updatedPassage = passageRepository.save(passage);

        // 4. 응답용 DTO 생성
        PassageSelectResponseDto selectedPassage = PassageSelectResponseDto.builder()
                .pasCode(updatedPassage.getPasCode())
                .title(updatedPassage.getTitle())
                .type(updatedPassage.getPasType())
                .keyword(updatedPassage.getKeyword())
                .content(updatedPassage.getContent())
                .gist(updatedPassage.getGist())
                .build();

        return selectedPassage;
    }

    // 지문 미리보기 리스트
    @Override
    public List<PassagePreviewListDto> getPreviewList(Long memCode) {
        List<PassageEntity> passages = passageRepository.findGeneratedPassagesByMember(memCode);

        if (passages.isEmpty()) {
            throw new EntityNotFoundException("지문이 존재하지 않습니다.");
        }

        List<PassagePreviewListDto> previews = passages.stream()
                .map(passage -> {
                    // date가 null인 경우 기본값 처리 (예시로 현재 날짜)
                    LocalDate date = passage.getDate() != null ? passage.getDate().toLocalDate() : LocalDate.now();

                    return PassagePreviewListDto.builder()
                            .passageCode(passage.getPasCode())  // 지문 코드
                            .passageTitle(passage.getTitle())   // 지문 제목
                            .subjectKeyword(passage.getKeyword()) // 지문 키워드
                            .date(date) // 날짜 처리
                            .favorite(passage.getIsFavorite()) // 즐겨찾기 상태
                            .build();
                })
                .collect(Collectors.toList());

        return previews;
    }

    // 지문 개별 조회
    @Override
    public PassageSelectResponseDto selectPassage(Long pasCode) {
        PassageEntity passageEntity = passageRepository.findById(pasCode)
                .orElseThrow(() -> new IllegalArgumentException("지문이 존재하지 않습니다."));

        PassageSelectResponseDto passage = PassageSelectResponseDto.builder()
                .pasCode(passageEntity.getPasCode())
                .title(passageEntity.getTitle())
                .type(passageEntity.getPasType())
                .keyword(passageEntity.getKeyword())
                .content(passageEntity.getContent())
                .gist(passageEntity.getGist())
                .build();

        return passage;

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


    //지문 + 문항 저장
    @Override
    @Transactional
    public PassageWithQuestionsResponseDto savePassageWithQuestions(Long memCode, PassageWithQuestionsRequestDto passageWithQuestionDto){
        try{
            System.out.println("자 출력해!2");
            System.out.println("코드"+memCode);
            // 사용자 정보 확인
            MemberEntity member = memberRepository.findById(memCode)
                    .orElseThrow(() -> new EntityNotFoundException("Member not found"));
            System.out.println("자 출력해!"+member);
            // 제목 중복 처리
            String title = generateTitle(passageWithQuestionDto.getTitle());

            // 지문 생성
            PassageEntity passage = PassageEntity.builder()
                    .pasType(passageWithQuestionDto.getType())
                    .keyword(passageWithQuestionDto.getKeyword())
                    .title(title)
                    .content(passageWithQuestionDto.getContent())
                    .gist(passageWithQuestionDto.getGist())
                    .date(LocalDateTime.now())
                    .isGenerated(passageWithQuestionDto.getIsGenerated())
                    .member(member)
                    .build();

            QuestionInsertRequestDto questionDto = passageWithQuestionDto.getQuestions();
            QuestionEntity question = QuestionEntity.builder()
                    .queQuery(questionDto.getQueQuery())
                    .queAnswer(questionDto.getQueAnswer())
                    .passage(passage)
                    .build();
            question.setQueOption(questionDto.getQueOption());

            //편의상 역방향 참조도 설정 (없어도 DB에는 저장됨)
            passage.setQuestions(question);
//
//            // 문항 추가
//            if(passageWithQuestionDto.getQuestions() != null){
//                QuestionInsertRequestDto questionInsertRequestDto = passageWithQuestionDto.getQuestions();
//                QuestionEntity question = QuestionEntity.builder()
//                        .queQuery(questionInsertRequestDto.getQueQuery())
//                        .queAnswer(questionInsertRequestDto.getQueAnswer())
//                        .passage(passage)
//                        .build();
//                // (수정) setter 메서드로 queOption 설정
//                question.setQueOption(questionInsertRequestDto.getQueOption());
//
//                // 지문과 문항 추가
//                passage.getQuestions().add(question);
//            }

            // 지문과 문항 저장
            PassageEntity savedPassage = passageRepository.save(passage);

            // 이용권 차감
            usageService.updateUsage(memCode, -1, "지문+문항 저장");

//            // 저장된 지문과 문항 반환
//            QuestionEntity savedQuestion = savedPassage.getQuestions().iterator().next();
//            // 문항 응답 DTO 생성
//            QuestionSelectResponseDto questionResponse = new QuestionSelectResponseDto(
//                    savedQuestion.getQueCode(),
//                    savedQuestion.getQueQuery(),
//                    savedQuestion.getQueOption(),
//                    savedQuestion.getQueAnswer()
//            );
            QuestionSelectResponseDto questionResponse = QuestionSelectResponseDto.builder()
                    .queCode(savedPassage.getQuestions().getQueCode())
                    .queQuery(savedPassage.getQuestions().getQueQuery())
                    .queAnswer(savedPassage.getQuestions().getQueAnswer())
                    .queOption(savedPassage.getQuestions().getQueOption())
                    .build();

            // PassageWithQuestionsResponseDto 생성
            PassageWithQuestionsResponseDto passageWithQuestionsResponseDto = PassageWithQuestionsResponseDto.builder()
                    .pasCode(savedPassage.getPasCode())
                    .title(savedPassage.getTitle())
                    .type(savedPassage.getPasType())
                    .keyword(savedPassage.getKeyword())
                    .content(savedPassage.getContent())
                    .gist(savedPassage.getGist())
                    .questions(questionResponse)  // 문항 응답 추가
                    .build();

            return passageWithQuestionsResponseDto;
        }catch (EntityNotFoundException e) {
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
}
