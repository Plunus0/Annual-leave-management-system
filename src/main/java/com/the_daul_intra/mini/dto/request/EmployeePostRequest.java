package com.the_daul_intra.mini.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    private Long annualCount;
    private String projectStatus;

}
