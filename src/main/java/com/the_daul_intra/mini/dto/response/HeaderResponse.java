package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeaderResponse {
    private Long adminId;
    private String adminName;
}
