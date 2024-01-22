package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.request.LoginRequest;
import com.the_daul_intra.mini.dto.response.LoginResponse;
import com.the_daul_intra.mini.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/admin")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/Login")
    public String login(@ModelAttribute LoginRequest  loginRequest, Model model, HttpSession session) {
        LoginResponse loginResponse = loginService.login(loginRequest.getEmail(), loginRequest.getPassword(),session);
        //위에 객체로 모델에 담아서 dashboard패이지로 이동
        if (loginResponse != null) {
            model.addAttribute("employee", loginResponse);

            return "index"; // 로그인 성공시 이동할 페이지
        } else {
            model.addAttribute("error", "로그인 실패: 이메일 또는 비밀번호를 확인해주세요.");

            return "Login"; // 로그인 실패시 이동할 페이지
        }



    }



    @GetMapping("/Login")
    public String showLoginPage(Model model) {
        model.addAttribute(model.addAttribute("loginRequest", new LoginRequest()));
        return "Login"; // 로그인 페이지를 보여주는 뷰
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션에서 사용자 정보 제거
        session.removeAttribute("employee");

        return "Login"; // 로그인 페이지를 보여주는 뷰
    }


}