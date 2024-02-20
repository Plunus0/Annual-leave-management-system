package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.configuration.Encryptor;
import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.dto.entity.YesNo;
import com.the_daul_intra.mini.dto.response.EmployeeDetailResponse;
import com.the_daul_intra.mini.repository.EmployeeProfileRepository;
import com.the_daul_intra.mini.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmployeeProfileService {


    private EmployeeProfileRepository employeeProfileRepository;
    private final EmployeeRepository employeeRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


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

        System.out.println(Encryptor.decrypt("RRN? : "+employeeProfile.getResidentRegistrationNumber()));

        assert employeeProfile != null;
        response.setId(employee.getId());
        response.setEmployeeNumber(employeeProfile.getEmployeeNumber());
        response.setEmail(employee.getEmail());
        response.setName(employeeProfile.getName());
        response.setRrn(Encryptor.decrypt(employeeProfile.getResidentRegistrationNumber()));
        response.setContactInfo(employeeProfile.getContactInformation());
        response.setPosition(employeeProfile.getPosition());
        if (employeeProfile.getJoinDate() != null) {
            response.setJoinDate(employeeProfile.getJoinDate().format(formatter));
        }
        if (employeeProfile.getRetirementDate() != null) {
            response.setRetirementDate(employeeProfile.getRetirementDate().format(formatter));
        }else{
            response.setRetirementDate("재직중");
        }
        response.setAdminStatus(String.valueOf(employee.getAdminStatus()));
        response.setAnnualCount(employeeProfile.getAnnualQuantity());
        response.setProjectStatus(String.valueOf(employeeProfile.getProjectStatus()));
        response.setAddress(employeeProfile.getAddress());
        response.setComment(employeeProfile.getAdminComment());

        return response;
    }


    public void saveEmployee(EmployeeProfile employee) {

        employeeProfileRepository.save(employee);
    }

    public void deleteEmployee(Long id) {

        employeeProfileRepository.deleteById(id);
    }

    public void updateEmployeeDetail(Long id, EmployeeDetailResponse employeeDetail) {

        // id에 해당하는 Employee와 EmployeeProfile을 찾습니다.
        Employee employee = employeeRepository.findById(id).orElse(null);
        assert employee != null;
        EmployeeProfile employeeProfile = employee.getEmployeeProfile();

        // Employee와 EmployeeProfile의 정보를 employeeDetail 정보로 업데이트합니다.
        employeeProfile.setName(employeeDetail.getName());

        //passwrod가 null이 아닐 경우에만 변경
        if (employeeDetail.getPassword() != null) {
                employee.setPassword(encoder.encode(employeeDetail.getPassword()));
        }

        employee.setEmail(employeeDetail.getEmail());
        employeeProfile.setResidentRegistrationNumber(Encryptor.encrypt(employeeDetail.getRrn()));
        employeeProfile.setPosition(employeeDetail.getPosition());
        employeeProfile.setEmployeeNumber(employeeDetail.getEmployeeNumber());
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
        employeeProfile.setAdminComment(employeeDetail.getComment());

        // employeeProfile의 projectStatus 설정
        if (employeeDetail.getProjectStatus() != null) {
            employeeProfile.setProjectStatus(YesNo.valueOf(employeeDetail.getProjectStatus()));
        } else {
            // projectStatus가 null일 경우 기본값으로 N 설정
            employeeProfile.setProjectStatus(YesNo.N);
        }
        // employee의 adminStatus 설정
        if (employeeDetail.getAdminStatus() != null) {
            employee.setAdminStatus(YesNo.valueOf(employeeDetail.getAdminStatus()));
        } else {
            // adminStatus가 null일 경우 기본값으로 N 설정
            employee.setAdminStatus(YesNo.N);
        }
        //AunnualCount 미입력시 설정
        if (employeeDetail.getAnnualCount() != null) {
            employeeProfile.setAnnualQuantity(employeeDetail.getAnnualCount());
        } else {
            employeeProfile.setAnnualQuantity(0L);
        }

        // 업데이트된 정보를 데이터베이스에 저장합니다.
        employeeRepository.save(employee);
        employeeProfileRepository.save(employeeProfile);
    }
}
