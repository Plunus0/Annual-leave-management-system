package com.the_daul_intra.mini.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public void authorActive(){
        //authentication객체에 SecurityContextHolder를 담아서 인증정보를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("index : " + authentication);
    }
    public void removeTokenCookie(HttpServletResponse httpResponse) {
        Cookie cookie = new Cookie("daulJwt", null);
        cookie.setMaxAge(0); // 쿠키의 유효기간을 0으로 설정하여 제거
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpResponse.addCookie(cookie);
    }

}