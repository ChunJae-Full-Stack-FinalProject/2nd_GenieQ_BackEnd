package com.cj.genieq.passage.entity;

import com.cj.genieq.member.entity.MemberEntity;
import com.cj.genieq.question.entity.QuestionEntity;
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
@SequenceGenerator(name = "seqPasNo", sequenceName = "SEQ_PAS_NO", allocationSize = 1, initialValue = 100)
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

    // 지문 하나에 문항 여러개
    @OneToOne(mappedBy = "passage", cascade = CascadeType.ALL, orphanRemoval = true)
    private QuestionEntity questions;

    // toString() 메소드 오버라이드
    @Override
    public String toString() {
        return "PassageEntity{" +
                "pasCode=" + pasCode +
                ", pasType='" + pasType + '\'' +
                ", keyword='" + keyword + '\'' +
                ", title='" + title + '\'' +
                ", content='" + (content != null ? content.substring(0, Math.min(content.length(), 30)) : "") + "..." + '\'' + // 내용 일부만 출력
                ", gist='" + gist + '\'' +
                ", date=" + date +
                ", isFavorite=" + isFavorite +
                ", isDeleted=" + isDeleted +
                ", isGenerated=" + isGenerated +
                '}';
    }
}
