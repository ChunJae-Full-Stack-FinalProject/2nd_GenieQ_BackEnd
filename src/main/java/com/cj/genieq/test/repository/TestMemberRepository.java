package com.cj.genieq.test.repository;

import com.cj.genieq.test.entity.TestMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestMemberRepository extends JpaRepository<TestMemberEntity,Long> {

    Optional<TestMemberEntity> findByMemberCode(Long memberCode);


}
