package com.cj.genieq.passage.repository;

import com.cj.genieq.passage.entity.PassageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassageRepository extends JpaRepository<PassageEntity,Long> {

}
