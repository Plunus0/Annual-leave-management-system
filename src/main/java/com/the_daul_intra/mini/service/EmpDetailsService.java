package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.EmpDetails;
import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.repository.ApiEmpLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmpDetailsService implements UserDetailsService {
    private final ApiEmpLoginRepository apiEmpLoginRepository;

    //인증정보에 담을 UserDetails 객체 생성 메서드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = apiEmpLoginRepository.findActiveByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("등록되어 있지 않은 직원입니다."));

        // UserDetails 객체 생성 및 반환
        return new EmpDetails(employee.getId(), employee.getEmail(), employee.getPassword(), getAuthorities(employee));
    }

    //관리자 여부에 따른 권한 부여
    public Collection<? extends GrantedAuthority> getAuthorities(Employee employee) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        //ADMIN_STATUS 필드가 Y인 경우 관리자 권한 부여
        if (employee.getAdminStatus().name().equals("Y")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            System.out.println("is Admin Authority");
        } else {
            //직원에 대한 권한 설정 (선택적)
            authorities.add(new SimpleGrantedAuthority("ROLE_EMP"));
            System.out.println("is Employee Authority");
        }
        return authorities;
    }



}
