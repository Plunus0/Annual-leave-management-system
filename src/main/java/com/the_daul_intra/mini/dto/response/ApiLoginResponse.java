package com.the_daul_intra.mini.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiLoginResponse {
    private String token;
    private String name;
    private Long id;
}
