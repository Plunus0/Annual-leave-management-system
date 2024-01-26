package com.the_daul_intra.mini.dto.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "NOTICE")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    private Employee employee;

    @CreationTimestamp
    @Column(name = "REG_DATE")
    private LocalDateTime regDate;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

    @Column(name = "UNUSED_DATE")
    private LocalDateTime unusedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "UNUSED", columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private YesNo unused = YesNo.N;

    @OneToMany(mappedBy = "notice", fetch = FetchType.LAZY)
    private Set<NoticeReadStatus> readStatuses;
}
