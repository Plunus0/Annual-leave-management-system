package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.repository.EmployeeProfileRepository;
import com.the_daul_intra.mini.repository.LoginRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final LoginRepository loginRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    public LoginService(LoginRepository employeeRepository, EmployeeProfileRepository employeeProfileRepository) {
        this.loginRepository = employeeRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    public Employee login(String email, String password) {
        Employee employee = loginRepository.findByEmailAndPassword(email, password);

        if (employee == null) {
            throw new RuntimeException("사용자 정보가 존재하지 않습니다.");
        }

        EmployeeProfile employeeProfile = employeeProfileRepository.findById(employee.getId()).orElse(null);

        if (employeeProfile != null) {
            if (!"Y".equals(employeeProfile.getEmployee().getAdminStatus())) {
                throw new RuntimeException("관리자만 로그인할 수 있습니다.");
            }

            if (employeeProfile.getRetirementDate() != null) {
                throw new RuntimeException("퇴사한 사용자는 로그인할 수 없습니다.");
            }
        } else {
            throw new RuntimeException("사용자 프로필 정보가 존재하지 않습니다.");
        }

        // 세션에 사용자 정보 저장하는 코드 추가
        // 예: session.setAttribute("USER", employee);

        return employee;
    }
}