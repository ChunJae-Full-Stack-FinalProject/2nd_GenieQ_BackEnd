package com.cj.genieq.question.entity;

import com.cj.genieq.passage.entity.PassageEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder

@Entity
@Table(name = "QUESTION")
@SequenceGenerator(name = "seqQueNo", sequenceName = "SEQ_QUE_NO", allocationSize = 1, initialValue = 100)
public class QuestionEntity {
    // (추가) 구분자 상수 정의
    private static final String OPTION_DELIMITER = ";;;";

    @Id
    @GeneratedValue(generator = "seqQueNo", strategy = GenerationType.SEQUENCE)
    @Column(name = "QUE_CODE")
    private Long queCode;

    @Lob
    @Column(name = "QUE_QUERY")
    private String queQuery;

    // (수정) List<String> 타입을 직접 사용하지 않고, 문자열로 저장
    @Lob
    @Column(name = "QUE_OPTION")
    private String queOptionStr;

    @Lob
    @Column(name = "QUE_ANSWER")
    private String queAnswer;

    // (추가) @Transient로 영속화되지 않는 필드 추가
    @Transient
    private List<String> queOption;

    // fk관계 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAS_CODE")
    private PassageEntity passage;

    // (추가) 저장 전에 List<String>을 문자열로 변환
    @PrePersist
    @PreUpdate
    public void beforeSave() {
        System.out.println("QuestionEntity.beforeSave 호출: queOption=" + queOption);
        if (queOption != null) {
            queOptionStr = String.join(OPTION_DELIMITER, queOption);
            System.out.println("저장할 queOptionStr=" + queOptionStr);
        }
    }

    // (추가) 로드 후 문자열을 List<String>으로 변환
    @PostLoad
    public void afterLoad() {
        System.out.println("QuestionEntity.afterLoad 호출: queOptionStr=" + queOptionStr);
        if (queOptionStr != null && !queOptionStr.isEmpty()) {
            queOption = Arrays.asList(queOptionStr.split(OPTION_DELIMITER));
            System.out.println("변환된 queOption=" + queOption);
        } else {
            queOption = new ArrayList<>();
        }
    }

    // (추가) getter, setter 재정의
    public List<String> getQueOption() {
        if (queOption == null) {
            if (queOptionStr != null && !queOptionStr.isEmpty()) {
                queOption = Arrays.asList(queOptionStr.split(OPTION_DELIMITER));
            } else {
                queOption = new ArrayList<>();
            }
        }
        return queOption;
    }

    public void setQueOption(List<String> queOption) {
        this.queOption = queOption;
        if (queOption != null) {
            this.queOptionStr = String.join(OPTION_DELIMITER, queOption);
        } else {
            this.queOptionStr = null;
        }
    }
}