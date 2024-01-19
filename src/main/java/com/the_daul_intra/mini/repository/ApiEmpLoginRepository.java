package com.the_daul_intra.mini.repository;

import com.the_daul_intra.mini.dto.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiEmpLoginRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e " +
            "JOIN e.employeeProfile ep " +
            "WHERE e.email = :email " +
            "AND (ep.retirementDate > CURRENT_DATE OR ep.retirementDate IS NULL)")
    Optional<Employee> findActiveByEmail(String email);

    @Query("SELECT e FROM Employee e " +
            "JOIN e.employeeProfile ep " +
            "WHERE e.id = :id " +
            "AND (ep.retirementDate > CURRENT_DATE OR ep.retirementDate IS NULL)")
    Optional<Employee> findActiveById(Long id);
}
