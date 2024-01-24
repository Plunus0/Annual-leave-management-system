package com.the_daul_intra.mini.configuration;

import com.the_daul_intra.mini.service.EmpDetailsService;
import com.the_daul_intra.mini.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

        String method = request.getMethod();
        String path = request.getRequestURI();

        // POST 메서드이고 로그인 경로에 대한 요청인 경우 필터 적용을 건너뛰기
        if (("POST".equals(method)) && (path.equals("/api/login") || path.equals("/admin/login"))) {
            filterChain.doFilter(request, response);
            return;
        }

        //헤더에 AUTHORIZATION로 전송받은 데이터를 가져온다.
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.info("authorization = " + authorization);

        //AUTHORIZATION로 전송받은 데이터가 없거나 접두사가 "Daul "가 아니라면 return
        if (authorization == null || !authorization.startsWith("Daul ")) {
            logger.error("authorization nothing");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 상태 코드 설정
            response.setContentType("application/json"); // 컨텐트 타입 설정
            response.getWriter().write("{\"error\": \"인증만료: JWT 인증에 실패했습니다.\"}"); // JSON 형식의 에러 메시지 작성
            response.getWriter().flush();
            response.getWriter().close();
            filterChain.doFilter(request, response);
            return;
        }

        //token 꺼내기("Daul " 접두사 없애기)
        String token = authorization.split(" ")[1];

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
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증만료: JWT 인증에 실패했습니다.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 상태 코드 설정
                response.setContentType("application/json"); // 컨텐트 타입 설정
                response.getWriter().write("{\"error\": \"인증만료: JWT 인증에 실패했습니다.\"}"); // JSON 형식의 에러 메시지 작성
                response.getWriter().flush();
                response.getWriter().close();
            }
        } catch (ExpiredJwtException e) {
            // JWT 토큰 만료 예외 처리(401 Unauthorized)
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증만료: JWT 인증에 실패했습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 상태 코드 설정
            response.setContentType("application/json"); // 컨텐트 타입 설정
            response.getWriter().write("{\"error\": \"인증만료: JWT 인증에 실패했습니다.\"}"); // JSON 형식의 에러 메시지 작성
            response.getWriter().flush();
            response.getWriter().close();
        } catch (JwtException e) {
            // 기타 JWT 관련 예외 처리(403 Forbidden)
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "유효하지 않은 토큰: JWT 인증 실패");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 상태 코드 설정
            response.setContentType("application/json"); // 컨텐트 타입 설정
            response.getWriter().write("{\"error\": \"유효하지 않은 토큰: JWT 인증 실패\"}"); // JSON 형식의 에러 메시지 작성
            response.getWriter().flush();
            response.getWriter().close();
        }

        filterChain.doFilter(request, response);
    }

}
