package com.the_daul_intra.mini.dto.entity;


import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "REG_DATE", length = 19)
    private LocalDateTime regDate;

    @Column(name = "UPDATE_DATE", length = 19)
    private LocalDateTime updateDate;

    @Column(name = "UNUSED_DATE", length = 19)
    private LocalDateTime unusedDate;

    @Enumerated(EnumType.STRING)
    private YesNo unused;

    @OneToMany(mappedBy = "notice", fetch = FetchType.LAZY)
    private Set<NoticeReadStatus> readStatuses;
}
