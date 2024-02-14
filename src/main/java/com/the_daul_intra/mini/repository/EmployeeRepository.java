package com.the_daul_intra.mini.repository;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.YesNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByEmail(String email);
    List<Employee> findByEmployeeProfileRetirementDateIsNotNull();

    // adminStatus가 N이면서 retirementDate가 오늘 미만인 총 인원 수
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.adminStatus = :adminStatus " +
            "AND e.employeeProfile.retirementDate < :today")
    Integer countByAdminStatusAndRetirementDateBefore(@Param("adminStatus") YesNo adminStatus, @Param("today") LocalDateTime today);

    // adminStatus가 N이면서 retirementDate가 오늘 미만이고 projectStatus가 Y인 개수
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.adminStatus = :adminStatus " +
            "AND e.employeeProfile.retirementDate < :today " +
            "AND e.employeeProfile.projectStatus = :projectStatusY")
    Integer countByAdminStatusAndRetirementDateBeforeAndProjectStatusY(@Param("adminStatus") YesNo adminStatus, @Param("today") LocalDateTime today, @Param("projectStatusY") YesNo projectStatusY);

    // adminStatus가 N이면서 retirementDate가 오늘 미만이고 projectStatus가 N인 개수
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.adminStatus = :adminStatus " +
            "AND e.employeeProfile.retirementDate < :today " +
            "AND e.employeeProfile.projectStatus = :projectStatusN")
    Integer countByAdminStatusAndRetirementDateBeforeAndProjectStatusN(@Param("adminStatus") YesNo adminStatus, @Param("today") LocalDateTime today, @Param("projectStatusN") YesNo projectStatusN);
}


