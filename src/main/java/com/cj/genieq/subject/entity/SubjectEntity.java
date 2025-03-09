package com.cj.genieq.subject.entity;

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
@Table(name = "SUBJECT")
@SequenceGenerator(name = "seqSubNo", sequenceName = "SEQ_SUB_NO", allocationSize = 1)
public class SubjectEntity {
    @Id
    @GeneratedValue(generator = "seqSubNo", strategy = GenerationType.SEQUENCE)
    @Column(name = "SUB_CODE")
    private Long subCode;

    @Column(name = "SUB_TYPE")
    @Enumerated(EnumType.STRING)
    private SubjectType subType;

    @Column(name = "SUB_KEYWORD")
    private String subKeyword;

    @OneToMany(mappedBy = "subject")
    private List<PassageEntity> passages;

    public enum SubjectType {
        인문, 예술, 사회, 문화, 과학, 기술
    }


}
