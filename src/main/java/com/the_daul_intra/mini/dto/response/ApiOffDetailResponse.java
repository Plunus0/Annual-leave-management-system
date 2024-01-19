package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiOffDetailResponse {
    private Long id;
    private Long writerId;
    private String writerName;
    private String requestType;
    private String status;
    private String leavePeriod;
    private LocalDate[] useDates;
    private String regDate;
    private String receiveDate;
    private String confirmDate;
    private String receiveAdmin;
    private String confirmAdmin;
    private String reason;
    private String adminComment;
}