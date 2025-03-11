package com.cj.genieq.passage.repository;

import com.cj.genieq.passage.entity.PassageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PassageRepository extends JpaRepository<PassageEntity,Long> {
    @Query(value = """
        SELECT * FROM (
            SELECT p.*, ROWNUM AS rn
            FROM passage p
            WHERE p.mem_code = :memCode
            AND LOWER(p.pas_title) LIKE LOWER(:keyword)
            AND p.pas_is_deleted = 0
            AND ROWNUM <= :endRow
        )
        WHERE rn > :startRow
        """, nativeQuery = true)
    List<PassageEntity> findByMemCodeAndKeyword(
            @Param("memCode") Long memCode,
            @Param("keyword") String keyword,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow);

    // 제목이 중복되는지 확인
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM PassageEntity p WHERE p.title = :title")
    boolean existsByTitle(@Param("title") String title);
    
    // 지문 미리보기 리스트
    @Query("SELECT p FROM PassageEntity p WHERE p.member.memCode = :memCode AND p.isGenerated = 1 ORDER BY p.date DESC")
    List<PassageEntity> findGeneratedPassagesByMember(@Param("memCode") Long memCode);
}
