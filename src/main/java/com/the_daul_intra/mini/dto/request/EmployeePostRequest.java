package com.the_daul_intra.mini.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeePostRequest {

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
    private String comment;
    private Long annualCount = 0L;
    private String projectStatus;
    private String employeeNumber;

}
