package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailResponse {
    private Long id;
    private String name;
    private String password;
    private String adminStatus;
    private String rrn;
    private String email;
    private String position;
    private String joinDate;
    private String retirementDate;
    private String contactInfo;
    private String address;
    private String employeeNumber;
    private String projectStatus;
    private String comment;
    private Long annualCount;
    private String projectName;
    private String projectStation;
    private String projectStart;



}
