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
@Table(name = "PROJECT")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    private Employee employee;

    @Column(name = "PROJECT_NAME", nullable = false, columnDefinition = "TEXT")
    private String projectName;

    @Column(name = "PROJECT_STATION", columnDefinition = "TEXT")
    private String projectStation;

    @Column(name = "START_DATE", nullable = false, length = 19)
    private LocalDateTime startDate;

    @Column(name = "END_DATE", length = 19)
    private LocalDateTime endDate;
}