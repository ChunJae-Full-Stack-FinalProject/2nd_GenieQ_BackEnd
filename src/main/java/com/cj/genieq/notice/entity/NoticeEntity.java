package com.cj.genieq.notice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor

@Entity
@Table(name = "NOTICE")
@SequenceGenerator( name = "seqNotNo", sequenceName = "seq_not_no", allocationSize = 1)
public class NoticeEntity {
    @Id
    @GeneratedValue(generator = "seqNotNo", strategy = GenerationType.SEQUENCE)
    @Column(name = "NOT_CODE")
    private Long notCode;

    @Column(name = "NOT_TYPE")
    private String type;

    @Column(name = "NOT_TITLE")
    private String title;

    @Lob
    @Column(name = "NOT_CONTENT")
    private String content;

    @Column(name = "NOT_DATE")
    private LocalDate date;
}
