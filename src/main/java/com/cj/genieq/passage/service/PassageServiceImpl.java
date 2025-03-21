package com.cj.genieq.passage.service;

import com.cj.genieq.member.entity.MemberEntity;
import com.cj.genieq.member.repository.MemberRepository;
import com.cj.genieq.passage.dto.request.*;
import com.cj.genieq.passage.dto.response.*;
import com.cj.genieq.passage.entity.PassageEntity;
import com.cj.genieq.passage.repository.PassageRepository;
import com.cj.genieq.question.dto.request.QuestionUpdateRequestDto;
import com.cj.genieq.question.dto.response.QuestionSelectResponseDto;
import com.cj.genieq.question.service.QuestionService;
import com.cj.genieq.usage.service.UsageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassageServiceImpl implements PassageService {

    private final PassageRepository passageRepository;
    private final MemberRepository memberRepository;
    private final UsageService usageService;
    private final QuestionService questionService;

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

        // 2. 제목 수정이 발생한 경우에만 중복 검사 실행
        String title = passage.getTitle(); // 기존 제목 유지
        if (passageDto.getTitle() != null && !passage.getTitle().equals(passageDto.getTitle())) {
            // 제목이 수정된 경우에만 중복 검사 실행
            title = generateTitle(passageDto.getTitle());
        }

        // 3. 기존 지문 정보 수정
        passage.setTitle(title);
        passage.setContent(passageDto.getContent());
        passage.setDate(LocalDateTime.now());

        // 4. 지문 수정 후 저장
        PassageEntity updatedPassage = passageRepository.save(passage);

        // 5. 응답용 DTO 생성
        return PassageSelectResponseDto.builder()
                .pasCode(updatedPassage.getPasCode())
                .title(updatedPassage.getTitle())
                .type(updatedPassage.getPasType())
                .keyword(updatedPassage.getKeyword())
                .content(updatedPassage.getContent())
                .gist(updatedPassage.getGist())
                .build();
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
                            .content(passage.getContent())
                            .gist(passage.getGist())
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

    // 지문 + 문항 저장 (트랜잭션 적용)
    @Transactional
    public PassageWithQuestionsResponseDto  savePassageWithQuestions(Long memCode, PassageWithQuestionsRequestDto requestDto) {
        // 1. 회원 조회
        MemberEntity member = memberRepository.findById(memCode)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        // 2. 제목 중복 처리
        String title = generateTitle(requestDto.getTitle());

        // 3. 지문 엔티티 생성
        PassageEntity passage = PassageEntity.builder()
                .pasType(requestDto.getType())
                .keyword(requestDto.getKeyword())
                .title(title)
                .content(requestDto.getContent())
                .gist(requestDto.getGist())
                .date(LocalDateTime.now())
                .isGenerated(requestDto.getIsGenerated())
                .member(member)
                .build();

        // 4. 지문 저장
        PassageEntity savedPassage = passageRepository.save(passage);

        // 5. 문항 저장은 QuestionService에서 처리
        List<QuestionSelectResponseDto> questions = questionService.saveQuestions(savedPassage, requestDto.getQuestions());

        // 6. 사용량 처리
        usageService.updateUsage(memCode, -1, "문항 생성");

        PassageWithQuestionsResponseDto responseDto =PassageWithQuestionsResponseDto.builder()
                .pasCode(savedPassage.getPasCode())
                .title(savedPassage.getTitle())
                .type(savedPassage.getPasType())
                .keyword(savedPassage.getKeyword())
                .content(savedPassage.getContent())
                .gist(savedPassage.getGist())
                .questions(questions)  // 필요한 경우 응답에 맞게 변환
                .build();

        return responseDto;
    }

    // 지문 + 문항 조회
    @Transactional(readOnly = true)
    public PassageWithQuestionsResponseDto getPassageWithQuestions(Long pasCode) {
        // 1. 지문 + 문항 조회 (JOIN 처리)
        PassageEntity passage = passageRepository.findById(pasCode)
                .orElseThrow(() -> new IllegalArgumentException("지문이 존재하지 않습니다."));

        // 2. 엔티티 → DTO 변환
        // 지문+문항 조회하는 작업은 지문엔티티에 의존하므로 지문 서비스의 책임에 해당해서 문항 서비스에 메서드를 추가하지 않은 것이다.
        List<QuestionSelectResponseDto> questions = (passage.getQuestions() != null)
                ? passage.getQuestions().stream()
                .map(q -> QuestionSelectResponseDto.builder()
                        .queCode(q.getQueCode())
                        .queQuery(q.getQueQuery())
                        .queOption(q.getQueOption() != null ? List.of(q.getQueOption().split(",")) : new ArrayList<>()) // String → JSON 변환
                        .queAnswer(q.getQueAnswer())
                        .description(q.getQueDescription())
                        .build())
                .collect(Collectors.toList())
                : new ArrayList<>();

        // 3. 응답 DTO 생성 후 반환 값 변수에 저장
        PassageWithQuestionsResponseDto result = PassageWithQuestionsResponseDto.builder()
                .pasCode(passage.getPasCode())
                .type(passage.getPasType())
                .keyword(passage.getKeyword())
                .title(passage.getTitle())
                .content(passage.getContent())
                .gist(passage.getGist())
                .questions(questions) // 문항이 없을 경우 빈 리스트 반환
                .build();

        // 4. 변수 반환
        return result;
    }


    //지문 수정 + 문항 수정
    @Transactional
    public PassageWithQuestionsResponseDto updatePassage(Long memCode, Long pasCode, PassageWithQuestionsRequestDto requestDto) {
        //회원조회
        MemberEntity member = memberRepository.findById(memCode)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        // 1. 기존 지문 조회
        PassageEntity passage = passageRepository.findById(pasCode)
                .orElseThrow(() -> new IllegalArgumentException("지문이 존재하지 않습니다."));

        // 2. 지문 필드 수정 (null 값 무시)
        if (requestDto.getType() != null) {
            passage.setPasType(requestDto.getType());
        }
        if (requestDto.getKeyword() != null) {
            passage.setKeyword(requestDto.getKeyword());
        }
        if (requestDto.getTitle() != null) {
            passage.setTitle(requestDto.getTitle());
        }
        if (requestDto.getContent() != null) {
            passage.setContent(requestDto.getContent());
        }
        if (requestDto.getGist() != null) {
            passage.setGist(requestDto.getGist());
        }
        if (requestDto.getIsGenerated() != null) {
            passage.setIsGenerated(requestDto.getIsGenerated());
        }

        passage.setDate(LocalDateTime.now());

        // INSERT → UPDATE 변환 처리 추가
        List<QuestionUpdateRequestDto> questionDtos = requestDto.getQuestions().stream()
                .map(q -> QuestionUpdateRequestDto.builder()
                        .queCode(q.getQueCode())
                        .queQuery(q.getQueQuery())
                        .queOption(q.getQueOption())
                        .queAnswer(q.getQueAnswer())
                        .description(q.getDescription())
                        .build())
                .collect(Collectors.toList());

        //문항 수정 후 반환된 값 받아서 그대로 사용
        List<QuestionSelectResponseDto> updatedQuestions = questionService.updateQuestions(passage, questionDtos);

        // ✅ mode가 "generate" 또는 "recreate"일 때만 차감 (수정 시에는 차감 X)
        if ("generate".equals(requestDto.getMode()) || "recreate".equals(requestDto.getMode())) {
            usageService.updateUsage(memCode, -1, "문항 생성");
        }

        // 응답 DTO 생성 후 변수에 저장
        PassageWithQuestionsResponseDto responseDto = PassageWithQuestionsResponseDto.builder()
                .pasCode(passage.getPasCode())
                .title(passage.getTitle())
                .type(passage.getPasType())
                .keyword(passage.getKeyword())
                .content(passage.getContent())
                .gist(passage.getGist())
                .questions(updatedQuestions)
                .build();

        return responseDto;
    }

    // 자료실 메인화면 리스트(즐겨찾기+최근 작업)
    @Override
    public List<PassageStorageEachResponseDto> selectPassageListInStorage(Long memCode, Integer isFavorite, Integer rownum) {
        List<PassageEntity> passageEntities = passageRepository.selectPassageListInStorage(memCode, isFavorite, rownum);

        // 조회 결과가 없으면 빈 리스트 반환
        if (passageEntities == null || passageEntities.isEmpty()) {
            return Collections.emptyList();
        }

        List<PassageStorageEachResponseDto> passages = passageEntities.stream()
                .map(p -> PassageStorageEachResponseDto.builder()
                        .title(p.getTitle())
                        .keyword(p.getKeyword())
                        .isGenerated(p.getIsGenerated())
                        .date(p.getDate().toLocalDate())
                        .isFavorite(p.getIsFavorite())
                        .build())
                .collect(Collectors.toList());

        return passages;
    }

    // 즐겨찾기 리스트
    @Override
    public List<PassageStorageEachResponseDto> selectFavoriteList(Long memCode) {
        List<PassageEntity> passageEntities = passageRepository.selectTop150FavoritePassages(memCode);

        // 조회 결과가 없으면 빈 리스트 반환
        if (passageEntities == null || passageEntities.isEmpty()) {
            return Collections.emptyList();
        }

        List<PassageStorageEachResponseDto> passages = passageEntities.stream()
                .filter(p -> p.getIsDeleted() == 0) // isDeleted = 0 필터링
                .map(p -> PassageStorageEachResponseDto.builder()
                        .pasCode(p.getPasCode())
                        .title(p.getTitle())
                        .keyword(p.getKeyword())
                        .isGenerated(p.getIsGenerated())
                        .date(p.getDate().toLocalDate())
                        .isFavorite(p.getIsFavorite())
                        .build())
                .collect(Collectors.toList());

        return passages;
    }

    // 최근 작업 내역 리스트
    @Override
    public List<PassageStorageEachResponseDto> selectRecentList(Long memCode) {
        List<PassageEntity> passageEntities = passageRepository.selectTop150RecentPassages(memCode);
        System.out.println("받아온 데이터 크기:"+passageEntities.toArray().length);

        // 조회 결과가 없으면 빈 리스트 반환
        if (passageEntities == null || passageEntities.isEmpty()) {
            return Collections.emptyList();
        }

        List<PassageStorageEachResponseDto> passages = passageEntities.stream()
                .filter(p -> p.getIsDeleted() == 0) // isDeleted = 0 필터링
                .map(p -> PassageStorageEachResponseDto.builder()
                        .pasCode(p.getPasCode())
                        .title(p.getTitle())
                        .keyword(p.getKeyword())
                        .isGenerated(p.getIsGenerated())
                        .date(p.getDate().toLocalDate())
                        .isFavorite(p.getIsFavorite())
                        .build())
                .collect(Collectors.toList());

        return passages;
    }

    @Override
    public int countRecentChange(Long memCode) {

        int countRecentChange = passageRepository.countByMemberAndIsDeleted(memCode, 0);

        return countRecentChange;
    }

    // 지문 삭제
    @Transactional
    @Override
    public boolean deletePassage(PassageDeleteRequestDto requestDto) {
        List<Long> pasCodeList = requestDto.getPasCodeList();

        try {
            int updatedCount = passageRepository.updateIsDeletedByPasCodeList(pasCodeList);
            // 업데이트된 개수가 전달받은 리스트 크기와 같으면 성공
            return updatedCount == pasCodeList.size();
        } catch (Exception e) {
            // 삭제 실패 시 로그 기록
            System.err.println("삭제 실패: " + e.getMessage());
            return false;
        }
    }

    // 작업명(지문 이름) 변경
    @Transactional
    @Override
    public boolean updatePassageTitle(PassageUpdateTitleRequestDto requestDto) {
        if (requestDto.getTitle() == null || requestDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("수정할 제목이 없습니다.");
        }

        // 수정할 대상이 있는지 먼저 확인
        PassageEntity passage = passageRepository.findById(requestDto.getPasCode())
                .orElseThrow(() -> new IllegalArgumentException("해당 지문이 존재하지 않습니다."));

        // 같은 제목이면 쿼리 실행 방지
        if (passage.getTitle().equals(requestDto.getTitle())) {
            return false;
        }

        // 제목 중복 처리
        String title = generateTitle(requestDto.getTitle());

        // 수정 실행
        int updatedCount = passageRepository.updateTitleByPasCode(requestDto.getPasCode(),title);

        // 수정이 실패
        if (updatedCount == 0) {
            throw new IllegalStateException("지문 제목 수정에 실패했습니다.");
        }

        return true;
    }
}
