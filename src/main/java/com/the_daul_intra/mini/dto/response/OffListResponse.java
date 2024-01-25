package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OffListResponse {
    private Long id;
    private String name;
    private String contactInfo;
    private String applicationDate;
    private String requestType;
    private String comment;
    private String status;
}
