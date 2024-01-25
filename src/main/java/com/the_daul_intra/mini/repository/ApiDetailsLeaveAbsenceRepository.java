package com.the_daul_intra.mini.repository;

import com.the_daul_intra.mini.dto.entity.DetailsLeaveAbsence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiDetailsLeaveAbsenceRepository extends JpaRepository<DetailsLeaveAbsence, Long>, JpaSpecificationExecutor<DetailsLeaveAbsence> {
}