package com.cj.genieq.usage.repository;

import com.cj.genieq.usage.entity.UsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsageRepository extends JpaRepository<UsageEntity, Long> {

    @Query(value = """
        SELECT * FROM (
            SELECT u.*, ROWNUM AS rn
            FROM (
                SELECT u.*
                FROM USAGE u
                WHERE u.mem_code = :memCode
                AND TRUNC(u.usa_date) BETWEEN TRUNC(:startDate) AND TRUNC(:endDate)
                ORDER BY u.usa_code DESC
            ) u
            WHERE ROWNUM <= :endRow
        )
        WHERE rn > :startRow
        """, nativeQuery = true)
    List<UsageEntity> findByMemCodeAndDateRange(
            @Param("memCode") Long memCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow);

        @Query(value = """
        SELECT u.usa_balance
        FROM (
            SELECT u.usa_balance
            FROM usage u
            WHERE u.mem_code = :memCode
            ORDER BY u.usa_date DESC
        ) u
        WHERE ROWNUM = 1
    """, nativeQuery = true)
        Optional<Integer> findLatestBalanceByMemberCode(@Param("memCode") Long memCode);
}


