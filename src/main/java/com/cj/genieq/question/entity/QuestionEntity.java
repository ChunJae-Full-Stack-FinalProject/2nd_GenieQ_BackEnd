package com.cj.genieq.question.entity;

import jakarta.persistence.*;

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
    private String queOption;

    @Lob
    @Column(name = "QUE_ANSWER")
    private String queAnswer;

    @ManyToOne
    private Long pasCode;

}
