package com.cj.genieq.member.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.cj.genieq.usage.entity.UsageEntity;

import java.util.ArrayList;
import java.util.List;

@Table(name = "MEMBER")
@Entity // jpa에서 이 클래스가 db의 테이블과 매핑됨.(db에 테이블을 자동 생성)
@SequenceGenerator(
        name = "seqMemNo", // JPA에서 사용할 시퀀스
        sequenceName = "seq_mem_no", //db에서 생성한 시퀀스
        allocationSize = 1
)
@Data // lombok 어노테이션
@NoArgsConstructor // 기본생성자 자동 생성
@AllArgsConstructor //모든 필드를 포함하는 생성자 자동 생성
@Builder
public class MemberEntity {

    @Id //기본 키 지정
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqMemNo")
    @Column(name = "MEM_CODE")
    private Long memCode;

    @Column(name = "MEM_NAME", nullable = false)
    private String memName;

    @Column(name = "MEM_EMAIL", nullable = false)
    private String memEmail;

    @Column(name = "MEM_PASSWORD", nullable = false)
    private String memPassword;

    @Column(name = "MEM_GENDER")
    private String memGender;

    @Column(name = "MEM_TYPE")
    private String memType;

    @Column(name = "MEM_IS_DELETED", nullable = false)
    private int memIsDeleted;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<UsageEntity> usages = new ArrayList<>();
}
