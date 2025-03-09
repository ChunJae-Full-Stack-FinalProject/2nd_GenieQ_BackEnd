package com.cj.genieq.passage.entity;

import com.cj.genieq.subject.entity.SubjectEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder

@Entity
@Table(name = "PASSAGE")
@SequenceGenerator(name = "seqPasNo", sequenceName = "SEQ_PAS_NO", allocationSize = 1)
public class PassageEntity {
    @Id
    @GeneratedValue(generator = "seqPasNo", strategy = GenerationType.SEQUENCE)
    @Column(name = "PAS_CODE")
    private Long pasCode;

    @Column(name = "PAS_TITLE")
    private String title;

    @Column(name = "PAS_CONTENT")
    private String content;

    @Column(name = "PAS_GIST")
    private String gist;

    @Column(name = "PAS_DATE")
    private LocalDate date;

    @Column(name = "PAS_IS_FAVORITE")
    private Integer isFavorite;

    @Column(name = "PAS_IS_DELETED")
    private Integer isDeleted;

    @ManyToOne
    @JoinColumn(name = "SUB_CODE")
    private SubjectEntity subject;

    private Long memCode;
}
