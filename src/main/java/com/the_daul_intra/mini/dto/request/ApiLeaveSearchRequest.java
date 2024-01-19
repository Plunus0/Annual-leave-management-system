package com.the_daul_intra.mini.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiLeaveSearchRequest {
    private String status; // "신청", "접수", "처리완료", "반려" 중 하나 또는 null
    private String type; // "반차", "월차", "연차", "병가" 중 하나 또는 null
    private String regDate;
    private Long writerId;
}
