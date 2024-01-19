package com.the_daul_intra.mini.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiLeavePostRequest {
    private LocalDate[] useDates;
    private String type; // "반차", "월차", "연차", "병가" 중 하나 또는 null
    private String reason;
}
