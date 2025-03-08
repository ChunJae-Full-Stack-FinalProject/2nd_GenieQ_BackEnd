package com.cj.genieq.passage.service;

import com.cj.genieq.passage.dto.PassageRequestDto;
import com.cj.genieq.passage.entity.PassageEntity;
import com.cj.genieq.passage.repository.PassageRepository;
import com.cj.genieq.subject.entity.SubjectEntity;
import com.cj.genieq.subject.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PassageServiceImpl implements PassageService {

    private final PassageRepository passageRepository;
    private final SubjectRepository subjectRepository;

    @Override
    @Transactional
    public PassageRequestDto savePassage(PassageRequestDto passageDto) {
        try {
            // 주제 enum 타입 변환
            SubjectEntity.SubjectType subjectType = SubjectEntity.SubjectType.valueOf(passageDto.getType());

            // 주제 존재 확인 후 없으면 새로 생성
            SubjectEntity subject = subjectRepository
                    .findBySubTypeAndSubKeyword(subjectType, passageDto.getKeyword())
                    .orElseGet(() -> {
                        SubjectEntity newSubject = SubjectEntity.builder()
                                .subType(subjectType)
                                .subKeyword(passageDto.getKeyword())
                                .build();
                        return subjectRepository.save(newSubject);
                    });

            // Passage 엔티티 생성 및 저장
            PassageEntity passage = PassageEntity.builder()
                    .title(passageDto.getTitle())
                    .content(passageDto.getContent())
                    .gist(passageDto.getGist())
                    .favorite(0) // 기본값 설정
                    .subject(subject)
                    .memCode(passageDto.getMemCode())
                    .build();

            PassageEntity savedPassage = passageRepository.save(passage);

            // 저장 성공 시 PassageEntity → PassageRequestDto로 변환
            return PassageRequestDto.builder()
                    .passCode(savedPassage.getPasCode())
                    .title(savedPassage.getTitle())
                    .content(savedPassage.getContent())
                    .gist(savedPassage.getGist())
                    .type(savedPassage.getSubject().getSubType().name()) // Enum → String 변환
                    .keyword(savedPassage.getSubject().getSubKeyword())
                    .memCode(passageDto.getMemCode())
                    .build();

        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 출력
            return null;
        }
    }
}
