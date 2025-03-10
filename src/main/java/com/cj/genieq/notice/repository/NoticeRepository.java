package com.cj.genieq.notice.repository;

import com.cj.genieq.notice.entity.NoticeEntity;
import com.cj.genieq.passage.entity.PassageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    @Query(value = """
    SELECT * FROM (
        SELECT n.*, ROWNUM AS rn
        FROM (
            SELECT n.* 
            FROM notice n
            WHERE (:type = '전체' OR n.not_type = :type)
            ORDER BY n.not_date DESC
        ) n
        WHERE ROWNUM <= :endRow
    )
    WHERE rn > :startRow
    """, nativeQuery = true)
    List<NoticeEntity> selectByType(
            @Param("type") String type,
            @Param("startRow") Integer startRow,
            @Param("endRow") Integer endRow);
}
