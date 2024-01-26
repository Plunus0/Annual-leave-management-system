package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.dto.entity.YesNo;
import com.the_daul_intra.mini.dto.response.EmployeeDetailResponse;
import com.the_daul_intra.mini.repository.EmployeeProfileRepository;
import com.the_daul_intra.mini.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public void updateEmployeeDetail(Long id, EmployeeDetailResponse employeeDetail, boolean changePassword) {

        // id에 해당하는 Employee와 EmployeeProfile을 찾습니다.
        Employee employee = employeeRepository.findById(id).orElse(null);
        assert employee != null;
        EmployeeProfile employeeProfile = employee.getEmployeeProfile();

        // Employee와 EmployeeProfile의 정보를 employeeDetail 정보로 업데이트합니다.
        employeeProfile.setName(employeeDetail.getName());
        // 비밀번호 변경 체크박스가 체크된 경우에만 비밀번호를 업데이트합니다.
        if (changePassword) {
            employee.setPassword(employeeDetail.getPassword());
        }
        employee.setEmail(employeeDetail.getEmail());
        employeeProfile.setResidentRegistrationNumber(employeeDetail.getRrn());
        employeeProfile.setPosition(employeeDetail.getPosition());
        // 날짜 정보를 처리하기 위해서는 문자열을 LocalDate 객체로 변환하는 과정이 필요합니다.
        // joinDate 업데이트
        String joinDateString = employeeDetail.getJoinDate();
        if (joinDateString != null && !joinDateString.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            employeeProfile.setJoinDate(LocalDate.parse(joinDateString, formatter).atStartOfDay());
        } else {
            employeeProfile.setJoinDate(null);
        }

        // retirementDate 업데이트
        String retirementDateString = employeeDetail.getRetirementDate();
        if (retirementDateString != null && !retirementDateString.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            employeeProfile.setRetirementDate(LocalDate.parse(retirementDateString, formatter).atStartOfDay());
        } else {
            employeeProfile.setRetirementDate(null);
        }
        employeeProfile.setContactInformation(employeeDetail.getContactInfo());
        employeeProfile.setAddress(employeeDetail.getAddress());
        // 라디오 버튼의 값(Y, N)을 boolean으로 변환합니다.
        employeeProfile.setProjectStatus(YesNo.valueOf(employeeDetail.getProjectStatus()));
        employee.setAdminStatus(YesNo.valueOf(employeeDetail.getAdminStatus()));
        employeeProfile.setAdminComment(employeeDetail.getComment());
        employeeProfile.setAnnualQuantity(employeeDetail.getAnnualCount());

        // 업데이트된 정보를 데이터베이스에 저장합니다.
        employeeRepository.save(employee);
        employeeProfileRepository.save(employeeProfile);
    }
}
