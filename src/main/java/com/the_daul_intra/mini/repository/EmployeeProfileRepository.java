package com.the_daul_intra.mini.repository;

import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile , Long> {
    EmployeeProfile findByEmployeeProfile(String email);
    EmployeeProfile findByEmployeeProfile(String email, String password);

}
