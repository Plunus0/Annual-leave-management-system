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

import java.time.LocalDate;
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

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Transactional
    public Employee createEmployee(EmployeePostRequest request) {
        System.out.println("nameService : " + request.getName());
        // Employee 생성
        Employee employee = Employee.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .adminStatus(YesNo.valueOf(request.getAdminStatus()))
                .build();

        employee = employeeRepository.save(employee);

        System.out.println("nameService2 : " + request.getName());
        System.out.println("data : " + employee.getId()+employee.getAdminStatus());
        // EmployeeProfile 생성

        EmployeeProfile employeeProfile = EmployeeProfile.builder()
                .employee(employee)//필수
                .name(request.getName())//필수
                .email(request.getEmail())//필수
                .residentRegistrationNumber(request.getRrn())//됨
                .position(request.getPosition())//됨
                .joinDate(LocalDate.parse(request.getJoinDate()).atStartOfDay())
                .retirementDate(request.getRetirementDate() != null && !request.getRetirementDate().isEmpty()
                        ? LocalDate.parse(request.getRetirementDate()).atStartOfDay()
                        : null)
                .contactInformation(request.getContactInfo())//필수
                .address(request.getAddress())
                .projectStatus(YesNo.valueOf(request.getProjectStatus()))
                .adminComment(request.getComment())
                .annualQuantity(request.getAnnualCount())//필수
                .build();//마무리
        System.out.println("nameService3 : " + request.getName());
        employeeProfileRepository.save(employeeProfile);


        return employee;
    }
}
