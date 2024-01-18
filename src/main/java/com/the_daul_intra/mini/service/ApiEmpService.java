/*
package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.request.ApiLoginRequest;
import com.the_daul_intra.mini.exception.AppException;
import com.the_daul_intra.mini.exception.ErrorCode;
import com.the_daul_intra.mini.repository.ApiEmpLoginRepository;
import com.the_daul_intra.mini.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiEmpService{

    private final ApiEmpLoginRepository apiEmpLoginRepository;
    @Value("${jwt.secret}")
    private String secretKey;

        public String apiLogin(ApiLoginRequest dto){
            //1. 인증과정
            //1-1. 이메일 확인
            MemberLogin selectedMember = memberLoginRepository.findActiveByEmail(dto.getEmail())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, email + "이 없습니다."));
            //1-2. 비밀번호 확인
                if(!encoder.matches(password, selectedMember.getPassword())){
                throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 틀렸습니다.");
            }
            //인증성공시 토큰 발급
                return JwtUtil.createJwt(email, secretKey);
        }

}
*/
