package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String name;
    private Long id;


}