package com.cj.genieq.passage.entity;

import com.cj.genieq.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder

@Entity
@DynamicInsert
@Table(name = "PASSAGE")
@SequenceGenerator(name = "seqPasNo", sequenceName = "SEQ_PAS_NO", allocationSize = 1)
public class PassageEntity {
    @Id
    @GeneratedValue(generator = "seqPasNo", strategy = GenerationType.SEQUENCE)
    @Column(name = "PAS_CODE")
    private Long pasCode;

    @Column(name = "PAS_TYPE")
    private String pasType;

    @Column(name = "PAS_KEYWORD")
    private String keyword;

    @Column(name = "PAS_TITLE")
    private String title;

    @Lob
    @Column(name = "PAS_CONTENT")
    private String content;

    @Column(name = "PAS_GIST")
    private String gist;

    @Column(name = "PAS_DATE")
    private LocalDateTime date;

    @Column(name = "PAS_IS_FAVORITE")
    private Integer isFavorite;

    @Column(name = "PAS_IS_DELETED")
    private Integer isDeleted;

    @Column(name = "PAS_IS_GENERATED")
    private Integer isGenerated;

    @ManyToOne
    @JoinColumn(name = "MEM_CODE")
    private MemberEntity member;
}
