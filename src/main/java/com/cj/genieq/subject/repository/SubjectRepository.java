package com.cj.genieq.subject.repository;

import com.cj.genieq.subject.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    Optional<SubjectEntity> findBySubTypeAndSubKeyword(SubjectEntity.SubjectType subType, String subKeyword);
}
