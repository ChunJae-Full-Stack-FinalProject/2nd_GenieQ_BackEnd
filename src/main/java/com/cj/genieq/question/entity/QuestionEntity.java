package com.cj.genieq.question.entity;

import com.cj.genieq.passage.entity.PassageEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder

@Entity
@Table(name = "QUESTION")
@SequenceGenerator(name = "seqQueNo", sequenceName = "SEQ_QUE_NO", allocationSize = 1)
public class QuestionEntity {
    @Id
    @GeneratedValue(generator = "seqQueNo", strategy = GenerationType.SEQUENCE)
    @Column(name = "QUE_CODE")
    private Long queCode;

    @Lob
    @Column(name = "QUE_QUERY")
    private String queQuery;

    @Lob
    @Column(name = "QUE_OPTION")
    private List<String> queOption;

    @Lob
    @Column(name = "QUE_ANSWER")
    private String queAnswer;

    //fk관계 매핑
    @ManyToOne
    @JoinColumn(name = "PAS_CODE")
    private PassageEntity passage;

    public void setPassage(PassageEntity passageEntity) {
        this.passage = passageEntity;
    }

}
