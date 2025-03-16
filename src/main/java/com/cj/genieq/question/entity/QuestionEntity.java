package com.cj.genieq.question.entity;

import com.cj.genieq.passage.entity.PassageEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Entity
@DynamicInsert
@Table(name = "QUESTION")
@SequenceGenerator(name = "seqQueNo", sequenceName = "SEQ_QUE_NO", allocationSize = 1, initialValue = 100)
public class QuestionEntity {

    @Id
    @GeneratedValue(generator = "seqQueNo", strategy = GenerationType.SEQUENCE)
    @Column(name = "QUE_CODE")
    private Long queCode;

    @Lob
    @Column(name = "QUE_QUERY")
    private String queQuery; // 문항 질문

    @Lob
    @Column(name = "QUE_OPTION")
    private String queOption; // 문항 보기 (JSON으로 저장 가능)

    @Lob
    @Column(name = "QUE_ANSWER")
    private String queAnswer; // 정답

    @Column(name = "QUE_DESCRIPTION")
    private String queDescription; // 정답

    // 지문과의 매핑 (다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAS_CODE")
    private PassageEntity passage;
}
