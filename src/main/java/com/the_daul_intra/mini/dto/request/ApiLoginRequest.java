package com.the_daul_intra.mini.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiLoginRequest {
    private String email;
    private String password;
}
