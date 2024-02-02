package com.the_daul_intra.mini.configuration;

import com.the_daul_intra.mini.service.EmpDetailsService;
import com.the_daul_intra.mini.service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final EmpDetailsService empDetailsService;
    @Value("${jwt.secret}")
    private String secretKey;
    private String requestURI;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors();

        httpSecurity
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
/*      UserDetails로 id와 password를 인증
        httpSecurity
                .formLogin((form) -> form
                        .loginPage("/admin/login")
                        .defaultSuccessUrl("/admin/index")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);*/

        httpSecurity
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/js/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/login", "/admin/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/admin/login", "/admin/logout").permitAll()
                                .requestMatchers("admin/**").hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated()
//                  .anyRequest().permitAll();
                );

        httpSecurity
                .addFilterBefore(new JwtFilter(empDetailsService, secretKey), UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                .exceptionHandling((exceptions) -> exceptions
                        .accessDeniedHandler(new AccessDeniedHandler() {
                            @Override// 권한 문제가 발생했을 때 이 부분을 호출한다.
                            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                                requestURI = request.getRequestURI();

                                // 웹 페이지 요청에 대한 처리
                                if (requestURI.startsWith("/admin")) {
                                    response.sendRedirect("/admin/login?403");
//                                response.setStatus(403);
//                                response.setCharacterEncoding("utf-8");
//                                response.setContentType("text/html; charset=UTF-8");
//                                response.getWriter().write("권한이 없는 사용자입니다.");
                                }
                                // API 요청에 대한 처리
                                else {
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                }

                            }
                        })
                        .authenticationEntryPoint(new AuthenticationEntryPoint() {
                            @Override// 인증문제가 발생했을 때 이 부분을 호출한다.
                            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                                requestURI = request.getRequestURI();

                                // 웹 페이지 요청에 대한 처리
                                if (requestURI.startsWith("/admin")) {
                                    response.sendRedirect("/admin/login?401");
                                }
                                // API 요청에 대한 처리
                                else {
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                }
                            }
                        })
                );
        return httpSecurity.build();
    }
}
