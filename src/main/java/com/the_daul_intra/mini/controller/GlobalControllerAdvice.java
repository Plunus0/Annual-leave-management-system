package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.EmpDetails;
import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.exception.AppException;
import com.the_daul_intra.mini.exception.ErrorCode;
import com.the_daul_intra.mini.repository.ApiEmpLoginRepository;
import com.the_daul_intra.mini.service.ApiEmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {
    private final ApiEmpLoginRepository apiEmpLoginRepository;

    @ModelAttribute("adminName")
    public String adminName(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Employee emp = apiEmpLoginRepository.findActiveById(((EmpDetails) authentication.getPrincipal()).getEmpId())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, " 접속정보가 없습니다. 로그인화면으로 이동합니다."));
            return emp.getEmployeeProfile().getName();
        }
        return "Guest";
    }


}
