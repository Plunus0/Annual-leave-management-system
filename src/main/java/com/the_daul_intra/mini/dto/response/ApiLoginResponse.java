package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ApiLoginResponse {
    private String token;
    private String name;
    private Long id;
}
