package com.the_daul_intra.mini.repository;

import com.the_daul_intra.mini.dto.entity.DetailsLeaveDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ApiDetailsLeaveDateRepository extends JpaRepository<DetailsLeaveDate, Long>{

    // useDate가 오늘인 DetailsLeaveAbsence의 개수
    Integer countByUseDate(LocalDate date);

    // useDate가 오늘인 특정 absenceType의 DetailsLeaveAbsence 개수
    @Query("SELECT COUNT(d) FROM DetailsLeaveAbsence d JOIN d.leaveDates ld WHERE ld.useDate = :date AND d.absenceType = :type")
    Integer countByUseDateAndAbsenceType(@Param("date") LocalDate date, @Param("type") String type);
}
