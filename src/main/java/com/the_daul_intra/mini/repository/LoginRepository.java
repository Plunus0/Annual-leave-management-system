package com.the_daul_intra.mini.repository;

import com.the_daul_intra.mini.dto.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Employee, Long> {

    Employee findByEmail(String email);

    Employee findByEmailAndPassword(String email, String password);
}