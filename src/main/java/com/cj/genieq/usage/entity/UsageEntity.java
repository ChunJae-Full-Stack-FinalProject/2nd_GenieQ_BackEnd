package com.cj.genieq.usage.entity;

import com.cj.genieq.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor

@Entity
@Table(name="USAGE")
@SequenceGenerator(name = "seqUsaNo", sequenceName = "SEQ_USA_NO", allocationSize = 1)
public class UsageEntity {
    @Id
    @GeneratedValue(generator = "seqUsaNo", strategy = GenerationType.SEQUENCE)
    @Column(name = "USA_CODE")
    private Long usaCode;

    @Column(name = "USA_TYPE")
    private String usaType; // 내역 종류

    @Column(name = "USA_DATE")
    private LocalDateTime usaDate; // 내역 일자

    @Column(name = "USA_COUNT")
    private int usaCount; // 내역 횟수

    @Column(name = "USA_BALANCE")
    private int usaBalance; // 내역 잔액 (기본값 5)

    @ManyToOne
    @JoinColumn(name = "MEM_CODE")
    private MemberEntity member;
}
