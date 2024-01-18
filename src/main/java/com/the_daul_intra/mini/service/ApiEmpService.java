package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.request.ApiLoginRequest;
import com.the_daul_intra.mini.dto.response.ApiLoginResponse;
import com.the_daul_intra.mini.exception.AppException;
import com.the_daul_intra.mini.exception.ErrorCode;
import com.the_daul_intra.mini.repository.ApiEmpLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ApiEmpService{

    private final ApiEmpLoginRepository apiEmpLoginRepository;
//    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.secret}")
    private String secretKey;

    public ApiLoginResponse apiLogin(ApiLoginRequest dto){
        //1. 인증과정
        //1-1. 이메일 확인
        Employee selectedEmp = apiEmpLoginRepository.findActiveByEmail(dto.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, dto.getEmail() + " 해당하는 아이디가 없습니다."));
        //1-2. 비밀번호 확인
        if(!dto.getPassword().equals(selectedEmp.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호 오류입니다.");
        }
        //복호화하여 비밀번호 확인하기
/*        if(!encoder.matches(dto.getPassword(), selectedEmp.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 틀렸습니다.");
        }*/

        //인증성공시 반환
        return ApiLoginResponse.builder()
                .token(/*JwtUtil.createJwt(dto.getEmail(), secretKey)*/"token")
                .name(selectedEmp.getEmployeeProfile().getName())
                .id(selectedEmp.getId())
                .build();
    }


    //관리자 여부에 따른 권한 부여
/*    public Collection<? extends GrantedAuthority> getAuthorities(Employee employee) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        //ADMIN_STATUS 필드가 Y인 경우 관리자 권한 부여
        if (employee.getAdminStatus().equals("Y")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            System.out.println("is Admin");
        } else {
            //일반 사용자에 대한 권한 설정 (선택적)
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            System.out.println("is Employee");
        }
        return authorities;
    }*/

}
