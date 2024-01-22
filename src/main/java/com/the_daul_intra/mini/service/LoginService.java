package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.response.LoginResponse;
import com.the_daul_intra.mini.repository.EmployeeProfileRepository;
import com.the_daul_intra.mini.repository.LoginRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final LoginRepository loginRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    public LoginService(LoginRepository loginRepository, EmployeeProfileRepository employeeProfileRepository) {
        this.loginRepository = loginRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    public LoginResponse login(String email, String password, HttpSession session) {
/*
        Optional<Employee> optionalEmployee = Optional.ofNullable(loginRepository.findByEmail(email));

        if (optionalEmployee.isEmpty()) {
            throw new RuntimeException("등록된 사용자가 아닙니다.");
        }

        Employee employee = optionalEmployee.get();
        System.out.println(employee);

        if (!password.equals(employee.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        if (!"Y".equals(employee.getAdminStatus())) {
            throw new RuntimeException("관리자만 로그인할 수 있습니다.");
        }

        //받아온 이메일을 employee테이블에서 비교해서 없으면 등록된 사용자가 아닙니다.라는 에러 메세지
        //객체에 저장되어있는 패스워드와 사용자가 입력한 패스워드가 일치하는지 일치 시 토큰 이메일 아이디 담아서 return까지
        Optional<EmployeeProfile> optionalEmployeeProfile = employeeProfileRepository.findById(employee.getId());
        if (optionalEmployeeProfile.isEmpty() || optionalEmployeeProfile.get().getRetirementDate() != null) {
            throw new RuntimeException("퇴사한 사용자는 로그인할 수 없습니다.");
        }

        // 세션에 사용자 정보 저장
        session.setAttribute("USER", employee);

        // 토큰 생성 로직은 생략했습니다.
        String token = "generatedToken";

        return new LoginResponse(token, employee.getEmail(), employee.getId());*/
        return new LoginResponse("token", "test@email", 1L);
    }

}