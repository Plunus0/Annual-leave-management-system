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
@Table(name = "NOTICE_READ_STATUS")
public class NoticeReadStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTICE_ID", nullable = false)
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    private Employee employee;

    @Column(name = "READ_DATE", length = 19)
    private LocalDateTime readDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "IS_READ", nullable = false)
    private YesNo isRead;

    public void setReadDate(LocalDateTime readDate) {
        this.readDate = readDate;
    }
}
