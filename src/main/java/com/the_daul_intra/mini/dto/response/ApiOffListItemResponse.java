package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiOffListItemResponse {
    private Long id;
    private Long writerId;
    private String regDate;
    private String status;
    private String requestType;
}
