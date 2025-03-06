package com.cj.genieq.test.model.repository;

import com.cj.genieq.test.model.entity.TestMemberEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestMemberRepository extends JpaRepository<TestMemberEntity,Long> {

    Optional<TestMemberEntity> findByMemberCode(Long memberCode);


}
