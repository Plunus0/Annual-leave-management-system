package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.configuration.BCryptEncoder;
import com.the_daul_intra.mini.configuration.Encryptor;
import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.dto.entity.Notice;
import com.the_daul_intra.mini.dto.entity.YesNo;
import com.the_daul_intra.mini.dto.request.EmployeePostRequest;
import com.the_daul_intra.mini.dto.response.EmployeeListResponse;
import com.the_daul_intra.mini.repository.EmployeeProfileRepository;
import com.the_daul_intra.mini.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeProfileRepository employeeProfileRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Transactional
    public Employee createEmployee(EmployeePostRequest request) {

        // Employee 생성
        Employee employee = Employee.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .adminStatus(request.getAdminStatus() != null ? YesNo.valueOf(request.getAdminStatus()) : YesNo.N)
                .build();

        employee = employeeRepository.save(employee);

        // EmployeeProfile 생성

        EmployeeProfile employeeProfile = EmployeeProfile.builder()
                .employee(employee)//필수
                .name(request.getName())//필수
                .email(request.getEmail())//필수
                .residentRegistrationNumber(Encryptor.encrypt(request.getRrn()))
                .position(request.getPosition())
                .joinDate(LocalDate.parse(request.getJoinDate()).atStartOfDay())
                .retirementDate(request.getRetirementDate() != null && !request.getRetirementDate().isEmpty()
                        ? LocalDate.parse(request.getRetirementDate()).atStartOfDay()
                        : null)
                .contactInformation(request.getContactInfo())//필수
                .address(request.getAddress())
                .projectStatus(request.getProjectStatus() != null ? YesNo.valueOf(request.getProjectStatus()) : YesNo.N)
                .adminComment(request.getComment())
                .annualQuantity(request.getAnnualCount() != null ? request.getAnnualCount() : 0L)
                .build();//마무리

        employeeProfileRepository.save(employeeProfile);

        return employee;
    }

    public Page<EmployeeListResponse> getEmpPagingList(Integer page, Integer size, String retire){
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("joinDate").ascending());

        // 'retire' 값에 따른 Specification 조건 생성
        Specification<EmployeeProfile> spec = (root, query, criteriaBuilder) -> {
            if ("Y".equals(retire)) {
                return criteriaBuilder.isNotNull(root.get("retirementDate"));
            } else {
                // 'retire'가 "N"이거나 null인 경우
                return criteriaBuilder.isNull(root.get("retirementDate"));
            }
        };

        Page<EmployeeProfile> empPage = employeeProfileRepository.findAll(spec, pageable);

        long totalList = empPage.getTotalElements();
        // 현재 페이지의 첫 번째 공지사항 번호
        AtomicInteger startNumber = new AtomicInteger((int) totalList - (page - 1) * size);

        return empPage.map(empList -> new EmployeeListResponse(
                empList.getId(),
                (long) startNumber.getAndDecrement(),
                empList.getPosition(),
                empList.getName(),
                empList.getContactInformation(),
                empList.getEmail(),
                empList.getProjectStatus()
        ));
    }
}
