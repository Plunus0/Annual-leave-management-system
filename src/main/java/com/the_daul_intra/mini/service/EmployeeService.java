package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.dto.entity.YesNo;
import com.the_daul_intra.mini.dto.request.EmployeePostRequest;
import com.the_daul_intra.mini.repository.EmployeeProfileRepository;
import com.the_daul_intra.mini.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeProfileRepository employeeProfileRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    public static List<Employee> findAll() {

        return null;
    }

    @Transactional
    public Employee createEmployee(EmployeePostRequest request) {

        // Employee 생성
        Employee employee = Employee.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .adminStatus(YesNo.valueOf(request.getAdminStatus()))
                .build();

        employee = employeeRepository.save(employee);

        // EmployeeProfile 생성
        EmployeeProfile employeeProfile = EmployeeProfile.builder()
                .employee(employee)
                .name(request.getName())
                .email(request.getEmail())
                .residentRegistrationNumber(request.getRrn())
                .position(request.getPosition())
                .joinDate(LocalDateTime.parse(request.getJoinDate()))
                .retirementDate(LocalDateTime.parse(request.getRetirementDate()))
                .contactInformation(request.getContactInfo())
                .address(request.getAddress())
                .projectStatus(YesNo.valueOf(request.getProjectStatus()))
                .adminComment(request.getComment())
                .annualQuantity(request.getAnnualCount())
                .build();

        employeeProfileRepository.save(employeeProfile);

        return employee;
    }
}
