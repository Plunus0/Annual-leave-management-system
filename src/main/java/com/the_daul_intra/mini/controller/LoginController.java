package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.request.LoginRequest;
import com.the_daul_intra.mini.dto.response.LoginResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoginController {
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        // 로그인 처리 로직
        // 아래는 예시로, 실제로는 데이터베이스에서 사용자를 조회하고 인증을 수행해야 합니다.
        if ("test@example.com".equals(request.getEmail()) && "password".equals(request.getPassword())) {
            LoginResponse response = new LoginResponse();
            response.setToken("token");
            response.setName("name");
            response.setId(1L);
            return response;
        } else {
            throw new RuntimeException("Invalid email or password");
        }
    }
}