package com.cj.genieq.test.entity;

import com.cj.genieq.test.dto.TestMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "TEST_MEMBER")
@SequenceGenerator(name = "seqTestMemberCode", sequenceName = "SEQ_TESTMEMBER_CODE",allocationSize = 1)
public class TestMemberEntity {
    @Id
    @GeneratedValue(generator = "seqTestMemberCode", strategy = GenerationType.SEQUENCE)
    private Long memberCode;
    @Column(nullable = false, unique = true)
    private String name;
    private Integer age;
    private LocalDate birthday;

    public TestMember toTestMember(){
        return TestMember.builder().memberCode(memberCode).name(name).age(age).birthday(birthday).build();
    }
    public static TestMemberEntity fromTestMember(TestMember testMember){
        return TestMemberEntity.builder()
                .memberCode(testMember.getMemberCode()).name(testMember.getName())
                        .age(testMember.getAge()).birthday(testMember.getBirthday()).build();
    }

}
