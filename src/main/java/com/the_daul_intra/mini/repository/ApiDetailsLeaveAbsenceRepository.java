package com.the_daul_intra.mini.repository;

import com.the_daul_intra.mini.dto.entity.DetailsLeaveAbsence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ApiDetailsLeaveAbsenceRepository extends JpaRepository<DetailsLeaveAbsence, Long>, JpaSpecificationExecutor<DetailsLeaveAbsence> {

    // processingStatus가 "승인"가 아닌 총 DetailsLeaveAbsence 개수
    Integer countByProcessingStatusNot(String status);

    // 특정 absenceType에 대한 개수를 세는 메서드
    Integer countByProcessingStatusNotAndAbsenceType(String status, String type);


}