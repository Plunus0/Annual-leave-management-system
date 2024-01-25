package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.EmpDetails;
import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.request.ApiLoginPostRequest;
import com.the_daul_intra.mini.dto.response.ApiLoginResponse;
import com.the_daul_intra.mini.exception.AppException;
import com.the_daul_intra.mini.exception.ErrorCode;
import com.the_daul_intra.mini.repository.ApiEmpLoginRepository;
import com.the_daul_intra.mini.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ApiEmpService {

    private final ApiEmpLoginRepository apiEmpLoginRepository;
    @Value("${jwt.secret}")
    private String secretKey;
    Long empId = null;
    Employee emp = null;

    public ApiLoginResponse apiGetLogin() {
        //토큰정보를 가져와서 인증 성공시 리턴 그게 아니라면 로그인화면으로 이동

        //authentication객체에 SecurityContextHolder를 담아서 인증정보를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication : "+authentication);
        //authentication에서 empId 추출
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            empId = ((EmpDetails) authentication.getPrincipal()).getEmpId();
        }
        // authentication에서 가져온 empId로 이메일을 추출하고 이메일 확인 시작 -> empId로 이메일 추출이 안될 경우 로그인 페이지로
        emp = apiEmpLoginRepository.findActiveById(empId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, empId + " 접속정보가 없습니다. 로그인화면으로 이동합니다."));

        //1. 이메일 확인  -> 이메일을 확인했는데 에러가 날 경우 로그인 페이지로
        Employee selectedEmp = apiEmpLoginRepository.findActiveByEmail(emp.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, emp.getEmail() + " 접속정보가 없습니다. 로그인화면으로 이동합니다."));
        System.out.println("selectedEmp_Email : "+selectedEmp.getEmail());

        return ApiLoginResponse.builder()
                .token(JwtUtil.createJwt(selectedEmp.getEmail(), secretKey))
                .name(selectedEmp.getEmployeeProfile().getName())
                .id(selectedEmp.getId())
                .build();
    }

    public ApiLoginResponse apiLogin(ApiLoginPostRequest request){
        //1. 인증과정
        //1-1. 이메일 확인

        Employee selectedEmp = apiEmpLoginRepository.findActiveByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, request.getEmail() + " 해당하는 아이디가 없습니다."));
        //1-2. 비밀번호 확인
        if(!request.getPassword().equals(selectedEmp.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다.");
        }
        //복호화하여 비밀번호 확인하기
/*        if(!encoder.matches(dto.getPassword(), selectedEmp.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 틀렸습니다.");
        }*/
        //인증성공시 반환
        return ApiLoginResponse.builder()
                .token(JwtUtil.createJwt(selectedEmp.getEmail(), secretKey))
                .name(selectedEmp.getEmployeeProfile().getName())
                .id(selectedEmp.getId())
                .build();
    }


}
