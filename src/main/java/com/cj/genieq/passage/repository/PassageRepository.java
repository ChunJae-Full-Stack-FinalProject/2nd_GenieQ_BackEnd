package com.cj.genieq.passage.repository;

import com.cj.genieq.member.entity.MemberEntity;
import com.cj.genieq.passage.entity.PassageEntity;
import com.itextpdf.commons.utils.JsonUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    @Query("SELECT p FROM PassageEntity p WHERE p.member.memCode = :memCode AND p.isGenerated = 1 AND p.isDeleted=0 ORDER BY p.date DESC")
    List<PassageEntity> findGeneratedPassagesByMember(@Param("memCode") Long memCode);

    // 자료실 메인에서 즐겨찾기/최근 작업 리스트
    @Query(value = "SELECT * FROM ( " +
            "    SELECT p.* FROM PASSAGE p " +
            "    WHERE p.MEM_CODE = :memCode " +
            "    AND (:isFavorite = 0 OR p.PAS_IS_FAVORITE = :isFavorite) " + // isFavorite이 1일 때만 조건 추가
            "    AND p.PAS_IS_DELETED = 0 " +
            "    ORDER BY p.PAS_DATE DESC " +
            ") WHERE ROWNUM <= :rn", nativeQuery = true)
    List<PassageEntity> selectPassageListInStorage(
            @Param("memCode") Long memCode,
            @Param("isFavorite") Integer isFavorite,
            @Param("rn") Integer rn
    );

    // 즐겨찾기 150개 리스트
    @Query(value = "SELECT * FROM ( " +
            "    SELECT p.* FROM PASSAGE p " +
            "    WHERE p.MEM_CODE = :memCode " +
            "    AND p.PAS_IS_FAVORITE = 1 " +
            "    ORDER BY p.PAS_DATE DESC " +
            ") WHERE ROWNUM <= 150 " +
            "AND PAS_IS_DELETED = 0", nativeQuery = true)
    List<PassageEntity> selectTop150FavoritePassages(
            @Param("memCode") Long memCode
    );

    // 최근 작업 150개 리스트
    @Query(value = "SELECT * FROM ( " +
            "    SELECT p.* FROM PASSAGE p " +
            "    WHERE p.MEM_CODE = :memCode AND p.PAS_IS_DELETED = 0" +
            "    ORDER BY p.PAS_DATE DESC " +
            ") WHERE ROWNUM <= 150", nativeQuery = true)
    List<PassageEntity> selectTop150RecentPassages(
            @Param("memCode") Long memCode
    );


    @Modifying
    @Query("UPDATE PassageEntity p " +
            "SET p.isDeleted = 1 " +
            "WHERE p.pasCode IN :pasCodeList")
    int updateIsDeletedByPasCodeList(@Param("pasCodeList") List<Long> pasCodeList);

    // 작업명(지문 이름) 변경
    @Modifying
    @Query("UPDATE PassageEntity p SET p.title = :title WHERE p.pasCode = :pasCode")
    int updateTitleByPasCode(@Param("pasCode") Long pasCode, @Param("title") String title);

    @Query("SELECT COUNT(p) FROM PassageEntity p WHERE p.member.memCode = :memCode AND p.isDeleted = :isDeleted")
    int countByMemberAndIsDeleted(@Param("memCode") Long memCode, @Param("isDeleted") Integer isDeleted);
}
