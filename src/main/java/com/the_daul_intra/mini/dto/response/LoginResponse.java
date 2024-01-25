package com.the_daul_intra.mini.dto.response;

import lombok.Data;


@Data
public class LoginResponse {

    private String token;
    private String name;
    private Long id;

    public LoginResponse(String token, String email, Long id) {

    }
}