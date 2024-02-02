package com.the_daul_intra.mini.configuration;

import com.the_daul_intra.mini.service.EmpDetailsService;
import com.the_daul_intra.mini.service.LoginService;
import com.the_daul_intra.mini.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final EmpDetailsService empDetailsService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //경로와 메서드 및 AUTHORIZATION 데이터를 가져온다.
        final String method = request.getMethod();
        final String path = request.getRequestURI();
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String cookieToken = getCookieValue(request, "daulJwt");

        logger.info("\nmethod : " + method + "\npath : " + path + "\nauthorization = " + authorization + "\nCookie " + cookieToken);

        // Header의 AUTHORIZATION이 비어있거나 접두사가 Daul이 아닌 경우 헤더에서 jwt를 추출하고 그렇지 않다면 쿠키에서 jwt를 추출
        String token = (authorization != null && authorization.startsWith("Daul ")) ? authorization.split(" ")[1] : cookieToken;

        try {
            if (token != null && !JwtUtil.isExpired(token, secretKey)) {
                String email = JwtUtil.getEmailFromToken(token, secretKey);
                UserDetails userDetails = empDetailsService.loadUserByUsername(email);

                //가져온 정보들을 authentication에 담아서 SecurityContextHolder에 저장
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.error("authorization ok : "+authentication);
            } else {
                // 토큰이 만료된 경우(401 Unauthorized)
                System.out.println("else 401");
                //접속 경로가 api일 경우 에러발생 그렇지 앉다면 토큰을 삭제한다.
                tokenExprired(path, response);
            }
        } catch (ExpiredJwtException e) {
            // JWT 토큰 만료 예외 처리(401 Unauthorized)
            System.out.println("catch 401");
            tokenExprired(path, response);
        } catch (JwtException e) {
            // 기타 JWT 관련 예외 처리(403 Forbidden)
            System.out.println("catch 403");
            tokenException(path, response, e);
        }

        filterChain.doFilter(request, response);

    }

    //쿠키에서 jwt를 추출하는 메서드
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    //토큰 만료 에러처리
    private void tokenExprired(String path, HttpServletResponse response) throws IOException {
        if(path.startsWith("/api")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }else {
            removeTokenCookie(response);
        }
    }

    //토큰 오류 에러처리
    private void tokenException(String path, HttpServletResponse response, JwtException e) throws IOException {
        if(path.startsWith("/api")){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }else {
            removeTokenCookie(response);
        }
    }

    public void removeTokenCookie(HttpServletResponse httpResponse) {
        Cookie cookie = new Cookie("daulJwt", null);
        cookie.setMaxAge(0); // 쿠키의 유효기간을 0으로 설정하여 제거
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpResponse.addCookie(cookie);
    }
}
