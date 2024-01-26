package com.the_daul_intra.mini.dto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "DETAILS_LEAVE_ABSENCE")
public class DetailsLeaveAbsence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    private Employee employee;

    @Column(name = "ABSENCE_LEAVE_PERIOD", nullable = false)
    private Long absenceLeavePeriod;

    @Column(name = "ABSENCE_TYPE", nullable = false, length = 10)
    private String absenceType;

    @Column(name = "APPLICATION_DATE", nullable = false)
    private LocalDateTime applicationDate = LocalDateTime.now(); // 현재 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEPTION_ADMIN")
    private Employee receptionAdmin;

    @Column(name = "RECEPTION_DATE", length = 19)
    private LocalDateTime receptionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCESSED_ADMIN")
    private Employee processedAdmin;

    @Column(name = "PROCESSED_DATE")
    private LocalDateTime processedDate;

    @Column(name = "PROCESSING_STATUS", nullable = false, length = 2)
    private String processingStatus;

    @Column(name = "APPLICANT_COMMENTS", columnDefinition = "TEXT")
    private String applicantComments;

    @Column(name = "ADMIN_COMMENT", columnDefinition = "TEXT")
    private String adminComment;

    @OneToMany(mappedBy = "leaveRequest", fetch = FetchType.LAZY)
    private Set<DetailsLeaveDate> leaveDates;
}
