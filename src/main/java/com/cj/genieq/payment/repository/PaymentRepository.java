package com.cj.genieq.payment.repository;

import com.cj.genieq.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    @Query(value = """
        SELECT * FROM (
            SELECT p.*, ROWNUM AS rn
            FROM (
                SELECT p.*
                FROM PAYMENT p
                WHERE p.mem_code = :memCode
                AND TRUNC(p.pay_date) BETWEEN TRUNC(:startDate) AND TRUNC(:endDate)
                ORDER BY p.pay_code DESC
            ) p
            WHERE ROWNUM <= :endRow
        )
        WHERE rn > :startRow
        """, nativeQuery = true)
    List<PaymentEntity> findByMemCodeAndDateRange(
            @Param("memCode") Long memCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow);
}
