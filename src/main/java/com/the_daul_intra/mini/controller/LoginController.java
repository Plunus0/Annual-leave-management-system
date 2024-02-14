package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.EmpDetails;
import com.the_daul_intra.mini.dto.request.ApiLoginPostRequest;
import com.the_daul_intra.mini.dto.request.LoginRequest;
import com.the_daul_intra.mini.dto.response.ApiLoginResponse;
import com.the_daul_intra.mini.dto.response.IndexResponse;
import com.the_daul_intra.mini.dto.response.LoginResponse;
import com.the_daul_intra.mini.service.IndexService;
import com.the_daul_intra.mini.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value="/admin")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final RestTemplate restTemplate;
    private final IndexService indexService;
    private final String apiUrl = "http://localhost:8090/api/login"; // API URL 설정
    private final String url = "http://thedaul.mavericksoft.xyz/api/login"; // 배포시 URL 설정


    @PostMapping(path = "/login", consumes = "application/x-www-form-urlencoded")
    public String login(@ModelAttribute  ApiLoginPostRequest request, HttpServletResponse httpResponse, Model model, RedirectAttributes redirectAttributes) {
        try {
            ResponseEntity<ApiLoginResponse> response = restTemplate.postForEntity(apiUrl, request, ApiLoginResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // 쿠키 생성 및 설정
                Cookie cookie = new Cookie("daulJwt", response.getBody().getToken());
                cookie.setMaxAge(60 * 60 * 24 * 30); // 한달
                cookie.setHttpOnly(true); // JS 접근 방지
                cookie.setPath("/"); // 전체 경로에 적용
                httpResponse.addCookie(cookie);

                // 로그인 성공: 사용자 정보를 모델에 추가하고 index 페이지로 리다이렉트
                model.addAttribute("employee", response.getBody());
                return "redirect:/admin/index";
            } else {
                // 로그인 실패: 실패 메시지를 설정하고 login 페이지로 리다이렉트
                redirectAttributes.addFlashAttribute("errorMessage", "로그인 실패: 이메일 또는 비밀번호를 확인해주세요.");
                return "redirect:/admin/login";
            }
        } catch (HttpClientErrorException e) {
            // API 요청 중 에러 발생: 에러 메시지를 설정하고 login 페이지로 리다이렉트
            redirectAttributes.addFlashAttribute("errorMessage", "로그인 실패: 이메일 또는 비밀번호를 다시 입력바랍니다.");
            return "redirect:/admin/login";
        }
    }

    @GetMapping("/index")
    public String index(Model model){
        IndexResponse indexData = indexService.getIndexData();
        model.addAttribute("indexData", indexData);
        return "index";
    }

    @GetMapping
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage(HttpServletResponse httpResponse, Model model) {
        //로그인한 사람은 index페이지를 호출
        //로그인 하지 않은 사람만 login페이지로 이동
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse httpResponse) {
        // 로그아웃 시 쿠키 내의 토큰 제거
        loginService.removeTokenCookie(httpResponse);
        return "redirect:/admin/login"; // 로그인 페이지로 리다이렉트
    }


}