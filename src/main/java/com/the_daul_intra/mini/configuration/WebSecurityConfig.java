package com.the_daul_intra.mini.configuration;

import com.the_daul_intra.mini.service.ApiEmpService;
import com.the_daul_intra.mini.service.EmpDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final EmpDetailsService empDetailsService;
    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(HttpMethod.POST, "/api/login", "/admin/Login").permitAll();
                    requests.requestMatchers(HttpMethod.GET, "/admin/Login").permitAll();
                    requests.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN");
                    requests.anyRequest().authenticated();
//                    requests.anyRequest().permitAll();
                })
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtFilter(empDetailsService, secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
