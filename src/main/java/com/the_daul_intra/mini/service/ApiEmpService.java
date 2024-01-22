package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.dto.request.ApiLoginPostRequest;
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
    @Value("${jwt.secret}")
    private String secretKey;
    Long empId = null;
    String email = null;

    public ApiLoginResponse apiGetLogin(){
        //토큰정보를 가져와서 인증 성공시 리턴 그게 아니라면 로그인화면으로 이동

        //authentication객체에 SecurityContextHolder를 담아서 인증정보를 가져온다.
/*        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //authentication에서 empId 추출
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            empId = ((empDetails) authentication.getPrincipal()).getEmpId();
        }

        // authentication에서 가져온 empId로 이메일을 추출하고 이메일 확인 시작 -> empId로 이메일 추출이 안된다? 로그인 페이지로




        //1. 이메일 확인  -> 이메일을 확인했는데 에러가 났다? 로그인 페이지로
        Employee selectedEmp = apiEmpLoginRepository.findActiveByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, email + " 접속정보가 없습니다. 로그인화면으로 이동합니다."));

        }*/

        return ApiLoginResponse.builder()
                .token(/*JwtUtil.createJwt(dto.getEmail(), secretKey)*/"2st token")
                .name(/*selectedEmp.getEmployeeProfile().getName()*/"테스트사용자")
                .id(/*selectedEmp.getId()*/empId)
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
                .token(/*JwtUtil.createJwt(dto.getEmail(), secretKey)*/"1st token")
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
