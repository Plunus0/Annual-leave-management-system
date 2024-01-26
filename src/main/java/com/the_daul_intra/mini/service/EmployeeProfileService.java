package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.dto.response.EmployeeDetailResponse;
import com.the_daul_intra.mini.repository.EmployeeProfileRepository;
import com.the_daul_intra.mini.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmployeeProfileService {


    private EmployeeProfileRepository employeeProfileRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeProfileService(EmployeeProfileRepository employeeProfileRepository, EmployeeRepository employeeRepository) {
        this.employeeProfileRepository = employeeProfileRepository;
        this.employeeRepository = employeeRepository;
    }


    public List<EmployeeProfile> getAllEmployees() {

        return employeeProfileRepository.findAll();
    }

    public EmployeeDetailResponse getEmployeeDetail(Long id) {
        // Employee 객체를 조회합니다.
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            // 적절한 예외 처리를 합니다.
        }

        // Employee 객체에서 EmployeeProfile 객체를 가져옵니다.
        assert employee != null;
        EmployeeProfile employeeProfile = employee.getEmployeeProfile();
        if (employeeProfile == null) {
            // 적절한 예외 처리를 합니다.
        }

        // EmployeeDetailResponse 객체를 생성하고, Employee와 EmployeeProfile의 정보를 설정합니다.
        EmployeeDetailResponse response = new EmployeeDetailResponse();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        response.setId(employee.getId());
        response.setEmail(employee.getEmail());
        assert employeeProfile != null;
        response.setName(employeeProfile.getName());
        response.setRrn(employeeProfile.getResidentRegistrationNumber());
        response.setContactInfo(employeeProfile.getContactInformation());
        response.setPosition(employeeProfile.getPosition());
        if (employeeProfile.getJoinDate() != null) {
            response.setJoinDate(employeeProfile.getJoinDate().format(formatter));
        }
        if (employeeProfile.getRetirementDate() != null) {
            response.setRetirementDate(employeeProfile.getRetirementDate().format(formatter));
        }
        response.setAdminStatus(String.valueOf(employee.getAdminStatus()));
        response.setAddress(employeeProfile.getAddress());
        response.setProjectStatus(String.valueOf(employeeProfile.getProjectStatus()));
        response.setComment(employeeProfile.getAdminComment());
        response.setAnnualCount(employeeProfile.getAnnualQuantity());

        return response;
    }


    public void saveEmployee(EmployeeProfile employee) {

        employeeProfileRepository.save(employee);
    }

    public void deleteEmployee(Long id) {

        employeeProfileRepository.deleteById(id);
    }
}
