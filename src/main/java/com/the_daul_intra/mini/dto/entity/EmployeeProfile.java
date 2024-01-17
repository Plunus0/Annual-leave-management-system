package com.the_daul_intra.mini.dto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "EMPLOYEE_PROFILE")
public class EmployeeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")
    private Employee employee;

    @Column(name = "NAME", nullable = false, length = 20)
    private String name;

    @Column(name = "E_MAIL", nullable = false, length = 200)
    private String email;

    @Column(name = "RESIDENT_REGISTRATION_INT", length = 200)
    private String residentRegistrationNumber; // 주민등록번호 (암호화 필요)

    @Column(name = "POSITION", length = 10)
    private String position;

    @Column(name = "JOIN_DATE", length = 19)
    private LocalDateTime joinDate;

    @Column(name = "RETIREMENT_DATE", length = 19)
    private LocalDateTime retirementDate;

    @Column(name = "CONTACT_INFORMATION", nullable = false, length = 20)
    private String contactInformation;

    @Column(name = "ADDRESS", length = 100)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROJECT_STATUS")
    private YesNo projectStatus;

    @Column(name = "ADMIN_COMMENT", columnDefinition = "TEXT")
    private String adminComment;

    @Column(name = "ANNUAL_QUANTITY", nullable = false)
    private Integer annualQuantity;
}
