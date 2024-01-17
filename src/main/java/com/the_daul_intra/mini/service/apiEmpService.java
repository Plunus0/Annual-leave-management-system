package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.repository.apiEmpLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class apiEmpService{

    private final apiEmpLoginRepository apiEmpLoginRepository;

        public String apiLogin(String email, String password){
            //1. 인증과정
            //1-1. 이메일 확인
            MemberLogin selectedMember = memberLoginRepository.findActiveByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, email + "이 없습니다."));
            //1-2. 비밀번호 확인
                if(!encoder.matches(password, selectedMember.getPassword())){
                throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 틀렸습니다.");
            }
            //인증성공시 토큰 발급
                return JwtUtil.createJwt(email, secretKey, expiredMs);
        }

}
